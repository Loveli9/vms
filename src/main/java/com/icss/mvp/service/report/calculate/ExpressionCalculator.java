package com.icss.mvp.service.report.calculate;

import com.icss.mvp.entity.common.response.Result;
import com.icss.mvp.entity.report.ReportKpiConfig;
import com.icss.mvp.utils.NumericUtils;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;

@Service
public class ExpressionCalculator {

    private static Logger LOG = Logger.getLogger(ExpressionCalculator.class);
    //    private static final Log LOG = LogFactory.getLog(ExpressionCalculator.class);

    /**
     * 自定义表达式
     */
    private static final List<String> CUSTOMIZE_EXPS = Arrays.asList("sum", "count", "avg", "datadiff", "max", "min",
                                                                     "dateformat");

    public Result<String> calculate(ReportKpiConfig reportKpiConfig, DataManager dataManager) {
        Result<String> result = new Result(true, null, "-");
        String exp = reportKpiConfig.getExpression();
        try {
            //检查表达式是否正确
            if (!legalExpression(exp)) {
                result.setSuccess(false);
                String error = String.format("指标计算表达式格式错误（指标=%s，表达式=%s）", reportKpiConfig.getKpiName(), exp);
                result.addError(error);
                LOG.error(error);
            } else {
                //转化为后缀表达式
                LinkedList<String> suffixes = suffixExpression(exp);

                //计算后缀表达式
                Result<String> calculateResult = calculateSuffixExpression(suffixes, dataManager);
                if (!calculateResult.isSuccess()) {
                    result.addError(String.format("指标计算失败（指标=%s，表达式=%s），", reportKpiConfig.getKpiName(), exp) + calculateResult.getResult());
                } else {
                    result.setData(calculateResult.getData());
                }
            }
        } catch (Exception e) {
            result.setSuccess(false);
            String error = String.format("计算时发生异常（指标=%s，表达式=%s）", reportKpiConfig.getKpiName(), exp);
            result.addError(error);
            result.setResult(error);
            LOG.error(error);
        }
        return result;
    }

    /**
     * 计算后缀表达式的值,如果出现错误，则返回失败信息(success=false,result=错误原因)
     *
     * @param suffixs
     * @param dataManager
     * @return 计算结果：成功{success=true,data=不为-}，失败{success=false,data="-"}
     */
    private Result<String> calculateSuffixExpression(LinkedList<String> suffixs, DataManager dataManager) {
        Result<String> result = new Result(true, null, "-");
        //计算后缀表达式
        LinkedBlockingDeque<String> deque = new LinkedBlockingDeque<>();
        if (suffixs.size() == 1) {
            Result singleVr = singleFieldOperater(deque, dataManager, suffixs.get(0));
            if (!singleVr.isSuccess()) {
                result.setSuccess(false);
                result.setData("-");
                result.setResult(singleVr.getResult());
            } else {
                Object obj = singleVr.getData();
                String val;
                if (obj instanceof Date) {
                    val = new SimpleDateFormat("yyyy-MM-dd").format(obj);
                } else {
                    val = obj.toString();
                }
                result.setData(val);
            }
            return result;
        }


        Iterator<String> iterator = suffixs.iterator();
        while (iterator.hasNext()) {
            String ex = iterator.next();
            if (CUSTOMIZE_EXPS.contains(ex.toLowerCase())) {
                ex = ex.toLowerCase();
            }
            switch (ex) {
                case "+":
                case "-":
                case "*":
                case "/":
                    //进行加减乘除运算,如果出现错误，则返回失败信息{success=false,result=错误原因}，正确{success==true},同时将结果推入deque
                    Result sampleVr = sampleOperate(deque, dataManager, ex);
                    // 表示数据异常，因此直接退出外层循环
                    if (!sampleVr.isSuccess()) {
                        result.setSuccess(false);
                        result.setResult(sampleVr.getResult());
                        return result;
                    }
                    break;
                case "count":
                    //进行COUNT运算,如果出现错误，则返回失败信息{success=false,result=错误原因}，正确{success==true},同时将结果推入deque
                    Result countVr = countOperate(deque, dataManager, iterator);
                    if (!countVr.isSuccess()) {
                        result.setSuccess(false);
                        result.setResult(countVr.getResult());
                        return result;
                    }
                    break;
                case "avg":
                    //进行avg运算,如果出现错误，则返回失败信息{success=false,result=错误原因}，正确{success==true},同时将结果推入deque
                    Result avgVr = avgOperate(deque, dataManager, iterator);
                    if (!avgVr.isSuccess()) {
                        result.setSuccess(false);
                        result.setResult(avgVr.getResult());
                        return result;
                    }
                    break;
                case "sum":
                    Result sumVr = sumOperate(deque, dataManager, iterator);
                    if (!sumVr.isSuccess()) {
                        result.setResult(sumVr.getResult());
                        result.setSuccess(false);
                        return result;
                    }
                    break;
                case "min":
                    Result minVr = minOperate(deque, dataManager, iterator);
                    if (!minVr.isSuccess()) {
                        result.setResult(minVr.getResult());
                        result.setSuccess(false);
                        return result;
                    }
                    break;
                case "max":
                    Result maxVr = maxOperate(deque, dataManager, iterator);
                    if (!maxVr.isSuccess()) {
                        result.setResult(maxVr.getResult());
                        result.setSuccess(false);
                        return result;
                    }
                    break;
                case "datediff":
                    Result dateDiff = dateDiffOperate(deque, dataManager, iterator);
                    if (!dateDiff.isSuccess()) {
                        result.setResult(dateDiff.getResult());
                        result.setSuccess(false);
                        return result;
                    }
                    break;
                case "&":
                    Result join = joinOperate(deque, dataManager, iterator);
                    if (!join.isSuccess()) {
                        result.setResult(join.getResult());
                        result.setSuccess(false);
                        return result;
                    }
                    break;
                case "dateformat":
                    Result format = formatOperate(deque, dataManager, iterator);
                    if (!format.isSuccess()) {
                        result.setResult(format.getResult());
                        result.setSuccess(false);
                        return result;
                    }
                    break;
                default:
                    deque.addFirst(ex);
            }
        }
        if (!deque.isEmpty()) {
            String ex = deque.pop();
            result.setData(ex);
        }
        return result;
    }

    private Result minOperate(LinkedBlockingDeque<String> deque, DataManager dataManager, Iterator<String> iterator) {
        Result result = new Result<>(true);
        String ex = null;
        if (iterator.hasNext()) {
            ex = iterator.next();
            List list = dataManager.get(ex);
            if (list != null) {
                if (!list.isEmpty()) {
                    Object min = null;
                    for (Object obj : list) {
                        if (min == null) {
                            min = obj;
                        } else {
                            if (((Comparable) min).compareTo((Comparable) obj) == 1) {
                                min = obj;
                            }
                        }
                    }
                    deque.addFirst(String.valueOf(min));
                } else {
                    deque.addFirst("0");
                }
            } else {
                result.setSuccess(false);
                result.setResult(String.format("MIN计算错误，统计数据不存在(源数据=%s)！", ex));
            }
        } else {
            result.setSuccess(false);
            result.setResult(String.format("MIN计算错误，MIN表达式中缺少统计数据(源数据=%s)！", ex));
        }
        return result;
    }

    private Result maxOperate(LinkedBlockingDeque<String> deque, DataManager dataManager, Iterator<String> iterator) {
        Result result = new Result<>(true);
        String ex = null;
        if (iterator.hasNext()) {
            ex = iterator.next();
            List list = dataManager.get(ex);
            if (list != null) {
                if (!list.isEmpty()) {
                    Object max = null;
                    for (Object obj : list) {
                        if (max == null) {
                            max = obj;
                        } else {
                            if (((Comparable) max).compareTo((Comparable) obj) == -1) {
                                max = obj;
                            }
                        }
                    }
                    deque.addFirst(String.valueOf(max));
                } else {
                    deque.addFirst("0");
                }
            } else {
                result.setSuccess(false);
                result.setResult(String.format("MAX计算错误，统计数据不存在(源数据=%s)！", ex));
            }
        } else {
            result.setSuccess(false);
            result.setResult(String.format("MAX计算错误，MAX表达式中缺少统计数据(源数据=%s)！", ex));
        }
        return result;
    }


    /**
     * 进行表达式单字段聚会,如果出现错误，则返回结果信息
     *
     * @param deque
     * @param dataManager
     * @param field
     * @return 结果信息，成功｛success=true｝,失败{success=false,result=错误原因}
     */
    private Result singleFieldOperater(LinkedBlockingDeque<String> deque, DataManager dataManager, String field) {
        Result result = new Result<>(true);
        if (dataManager.containsKey(field)) {
            List list = dataManager.get(field);
            if (list.isEmpty()) {
                result.setResult("计算错误，源数据无数据！");
                result.setSuccess(false);
            } else if (list.size() > 1) {
                result.setResult("数据错误，计算需要单个数据，但源数据为集合！");
                result.setSuccess(false);
            } else {
                result.setData(list.get(0));
            }
        } else {
            result.setResult("计算错误，单字段表达式中，字段不是有效的源数据字段！");
        }
        return result;
    }


    /**
     * 进行SUM运算,如果出现错误，则返回结果信息
     *
     * @param deque
     * @param dataManager
     * @param iterator
     * @return 结果信息，成功｛success=true｝,失败{success=false,result=错误原因}
     */
    private Result sumOperate(LinkedBlockingDeque<String> deque, DataManager dataManager, Iterator<String> iterator) {
        Result result = new Result<>(true);
        String ex = null;
        if (iterator.hasNext()) {
            ex = iterator.next();
            List list = dataManager.get(ex);
            if (list != null) {
                if (!list.isEmpty()) {
                    double sum = 0d;
                    for (Object obj : list) {
                        if (obj == null) {
                            continue;
                        }

                        Double val = NumericUtils.parseDoubleMute(obj);
                        if (val == null) {
                            result.setSuccess(false);
                            result.setResult(String.format("SUM计算错误，源数据不是有效的数值类型(字段=%s)！", ex));
                            return result;
                        } else {
                            sum += val;
                        }
                    }
                    deque.addFirst(String.valueOf(sum));
                } else {
                    deque.addFirst("0");
                }
            } else {
                result.setSuccess(false);
                result.setResult(String.format("AVG计算错误，统计数据不存在(源数据=%s)！", ex));
            }
        } else {
            result.setSuccess(false);
            result.setResult(String.format("AVG计算错误，AVG表达式中缺少统计数据(源数据=%s)！", ex));
        }
        return result;
    }

    /**
     * 进行AVG运算,如果出现错误，则返回结果信息
     *
     * @param deque
     * @param dataManager
     * @param iterator
     * @return 结果信息，成功｛success=true｝,失败{success=false,result=错误原因}
     */
    private Result avgOperate(LinkedBlockingDeque<String> deque, DataManager dataManager, Iterator<String> iterator) {
        Result result = new Result<>(true);
        String ex = null;
        if (iterator.hasNext()) {
            ex = iterator.next();
            List list = dataManager.get(ex);
            if (list != null) {
                if (!list.isEmpty()) {
                    double sum = 0d;
                    int count = 0;
                    for (Object obj : list) {
                        if (obj == null) {
                            continue;
                        }

                        Double val = NumericUtils.parseDoubleMute(obj);
                        if (val == null) {
                            result.setSuccess(false);
                            result.setResult(String.format("AVG计算错误，源数据不是有效的数值类型(字段=%s)！", ex));
                            return result;
                        }

                        sum += val;
                        count++;
                    }

                    BigDecimal avg = BigDecimal.valueOf(sum).divide(BigDecimal.valueOf(count), 4, BigDecimal.ROUND_HALF_UP);
                    deque.addFirst(String.valueOf(avg));
                } else {
                    deque.addFirst("0");
                }
            } else {
                result.setSuccess(false);
                result.setResult(String.format("AVG计算错误，统计数据不存在(源数据=%s)！", ex));
            }
        } else {
            result.setSuccess(false);
            result.setResult(String.format("AVG计算错误，表达式中缺少统计数据(源数据=%s)！", ex));
        }
        return result;
    }


    /**
     * 进行COUNT运算,如果出现错误，则返回结果信息
     *
     * @param deque
     * @param dataManager
     * @param iterator
     * @return 结果信息，成功｛success=true｝,失败{success=false,result=错误原因}
     */
    private Result countOperate(LinkedBlockingDeque<String> deque, DataManager dataManager, Iterator<String> iterator) {
        Result result = new Result<>(true);
        String ex = null;
        if (iterator.hasNext()) {
            ex = iterator.next();
            List list = dataManager.get(ex);
            if (list != null) {
                BigDecimal value = new BigDecimal(list.size());
                deque.addFirst(value.toString());
            } else {
                result.setSuccess(false);
                result.setResult(String.format("Count计算错误，统计数据不存在(源数据=%s)！", ex));
            }
        } else {
            result.setSuccess(false);
            result.setResult(String.format("Count计算错误，表达式中缺少统计数据(源数据=%s)！", ex));
        }
        return result;
    }

    /**
     * 进行日期相减，datediff(date1,date2,month) ||datediff(date1,date2,day)
     * 第一个日期与第二个日期之间的月份和日期差值
     *
     * @param deque
     * @param dataManager
     * @param iterator
     * @return
     */
    private Result dateDiffOperate(LinkedBlockingDeque<String> deque, DataManager dataManager, Iterator<String> iterator) {
        Result result = new Result<>(true);
        String ex = null;
        if (iterator.hasNext()) {
            ex = iterator.next();
            String[] exs = ex.split(",");
            if (exs.length != 3) {
                result.setSuccess(false);
                result.setResult(String.format("datediff计算错误，datediff表达式中参数个数不对,datediff(date1,date2,day)", ex));
            } else {
                String date1Field = exs[0];
                String date2Field = exs[1];
                String tag = exs[2].toLowerCase();
                if (!"month".equals(tag) && !"day".equals(tag)) {
                    result.setSuccess(false);
                    result.setResult(String.format("datediff计算错误，datediff表达式中第3个数不对,只能为day或者month！", ex));
                } else {
                    Date d1 = null;
                    Date d2 = null;
                    if (dataManager.containsKey(date1Field)) {
                        List<Object> dates1 = dataManager.get(date1Field);
                        d1 = (Date) dates1.get(0);
                    } else {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            d1 = format.parse(date1Field);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    if (dataManager.containsKey(date2Field)) {
                        List<Object> dates2 = dataManager.get(date2Field);
                        d2 = (Date) dates2.get(0);
                    } else {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            d2 = format.parse(date1Field);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    if ("month".equals(tag)) {
                        deque.addFirst(dateDiffMonth(d1, d2) + "");
                    } else if ("day".equals(tag)) {
                        deque.addFirst(dateDiffDay(d1, d2) + "");
                    }
                }
            }
        }
        return result;
    }

    /**
     * 进行日期相减，datediff(date1,date2,month) ||datediff(date1,date2,day)
     * 第一个日期与第二个日期之间的月份和日期差值
     *
     * @param deque
     * @param dataManager
     * @param iterator
     * @return
     */
    private Result joinOperate(LinkedBlockingDeque<String> deque, DataManager dataManager, Iterator<String> iterator) {
        Result result = new Result<>(true);
        String oneStr = deque.removeFirst();
        String anotherStr = deque.removeFirst();
        if (dataManager.containsKey(oneStr)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            Object d1 = dataManager.get(oneStr).get(0);
            if (d1 instanceof Date) {
                Date d = (Date) d1;
                oneStr = sdf.format(d);
            } else {
                oneStr = d1.toString();
            }
        }
        if (dataManager.containsKey(anotherStr)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            Object d1 = dataManager.get(anotherStr).get(0);
            if (d1 instanceof Date) {
                Date d = (Date) d1;
                anotherStr = sdf.format(d);
            } else {
                anotherStr = d1.toString();
            }
        }
        deque.addFirst(anotherStr + "~" + oneStr);
        return result;
    }

    /**
     * 日期格式化
     *
     * @param deque
     * @param dataManager
     * @param iterator
     * @return
     */
    private Result formatOperate(LinkedBlockingDeque<String> deque, DataManager dataManager, Iterator<String> iterator) {
        Result result = new Result<>(true);
        String ex = null;
        if (iterator.hasNext()) {
            ex = iterator.next();
            String[] exs = ex.split(",");
            if (exs.length != 2) {
                result.setSuccess(false);
                result.setResult(String.format("日期格式化计算错误，format表达式中参数个数不对,format(filed,pattern)", ex));
                return result;
            } else {
                String date1Field = exs[0];
                String pattern = exs[1];
                if ("".equals(pattern)) {
                    result.setSuccess(false);
                    result.setResult(String.format("format计算错误，format表达式中第二个参数为空！", ex));
                    return result;
                } else {
                    SimpleDateFormat format = new SimpleDateFormat(pattern);
                    Date d1 = null;
                    if (dataManager.containsKey(date1Field)) {
                        List<Object> dates1 = dataManager.get(date1Field);
                        Object o = dates1.get(0);
                        if (o instanceof Date) {
                            d1 = (Date) o;
                            deque.addFirst(format.format(d1));
                        } else {
                            result.setSuccess(false);
                            result.setResult(String.format("format计算错误，格式化数据不是时间类型", ex));
                            return result;
                        }
                    } else {
                        result.setSuccess(false);
                        result.setResult(String.format("format计算错误，未配置元数据！", ex));
                        return result;
                    }

                }
            }
        }
        return result;
    }

    /**
     * 检查表达式是否准确
     *
     * @param exp
     * @return
     */

    private boolean legalExpression(String exp) {
        if (exp == null || "".equals(exp.trim())) {
            return false;
        }
        Deque<Character> deque = new ArrayDeque<>();
        int len = exp.length();
        for (int i = 0; i < len; i++) {
            char c = exp.charAt(i);
            if (c == '(') {
                deque.addFirst(c);
                continue;
            }
            if (c == ')') {
                if (deque.isEmpty()) {
                    return false;
                }
                if (deque.removeFirst() != '(') {
                    return false;
                }
            }
        }
        if (!deque.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * 转化为后缀表达式
     *
     * @param exp
     * @return
     */
    private LinkedList<String> suffixExpression(String exp) {
        int len = exp.length();
        StringBuffer temp = new StringBuffer();
        LinkedList<String> result = new LinkedList<>();
        LinkedBlockingDeque<String> symbols = new LinkedBlockingDeque<>();
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < len; j++) {
            if (!result.isEmpty()) {
                String last = result.getLast();
                if ("dateformat".equals(last)) {
                    char c = exp.charAt(j);
                    if (c == '(') {
                        continue;
                    } else if (c == ')') {
                        result.add(sb.toString());
                        sb.delete(0, sb.length());
                        continue;
                    } else {
                        sb.append(c);
                        continue;
                    }
                }
            }
            char c = exp.charAt(j);
            String popStr;
            switch (c) {
                case '(':
                    symbols.push('(' + "");
                    break;
                case ')':
                    while (!symbols.isEmpty()) {
                        popStr = symbols.pop();
                        if (popStr.equals("(")) {
                            break;
                        } else {
                            result.add(popStr);
                        }
                    }
                    break;
                case '+':
                case '-':
                case '&':
                    while (!symbols.isEmpty()) {
                        popStr = symbols.pop();
                        if (popStr.equals("(")) {
                            symbols.push(popStr);
                            break;
                        } else {
                            result.add(popStr);
                        }
                    }
                    symbols.push(c + "");
                    break;
                case '*':
                case '/':
                    while (!symbols.isEmpty()) {
                        popStr = symbols.pop();
                        if (popStr.equals("+") || popStr.equals("-") || popStr.equals("(") || popStr.equals("&")) {
                            symbols.push(popStr);
                            break;
                        } else {
                            result.add(popStr);
                        }
                    }
                    symbols.push(c + "");
                    break;
                default:
                    temp.append(c);
                    int next = j + 1;
                    if (next == len) {
                        next = j;
                    }
                    char nextC = exp.charAt(next);
                    if (next == j || nextC == '(' || nextC == ')' || nextC == '+' || nextC == '-' || nextC == '*' || nextC == '/' || nextC == '&') {
                        result.add(temp.toString());
                        temp.delete(0, temp.length());
                    }
                    break;
            }
        }
        while (!symbols.isEmpty()) {
            result.add(symbols.pop());
        }
        return result;
    }


    /**
     * 进行加减乘除运算,如果出现错误，则返回结果信息
     *
     * @param deque
     * @param dataManager
     * @param oper
     * @return 结果信息，成功｛success=true｝,失败{success=false,result=错误原因}
     */
    private Result sampleOperate(LinkedBlockingDeque<String> deque, DataManager dataManager, String oper) {
        Result result = new Result<>(true);
        BigDecimal value = new BigDecimal(0);
        String oneStr = deque.removeFirst();
        String anotherStr = deque.removeFirst();

        Result<BigDecimal> vr1 = getSingleBigDecimal(dataManager, anotherStr);
        if (!vr1.isSuccess()) {
            result.setSuccess(false);
            result.setResult(vr1.getResult());
            return result;
        }
        Result<BigDecimal> vr2 = getSingleBigDecimal(dataManager, oneStr);
        if (!vr2.isSuccess()) {
            result.setSuccess(false);
            result.setResult(vr1.getResult());
            return result;
        }
        if ("+".equals(oper)) {
            value = vr1.getData().add(vr2.getData());
        } else if ("-".equals(oper)) {
            value = vr1.getData().subtract(vr2.getData());
        } else if ("*".equals(oper)) {
            value = vr1.getData().multiply(vr2.getData());
        } else if ("/".equals(oper)) {
            if (vr1.getData().doubleValue() == 0) {
                value = new BigDecimal(0);
            } else if (vr2.getData().doubleValue() == 0) {
                result.setSuccess(false);
                result.setResult("数据错误，除数不允许为零！");
                return result;
            } else {
                value = vr1.getData().divide(vr2.getData(), 4, BigDecimal.ROUND_HALF_UP);
            }
        }
        deque.addFirst(value.toString());
        return result;
    }

    /**
     * 获取一个单的数据数据，如果数据管理器中的数据为多个值，则返回失败信息(success=false,result=错误原因)
     *
     * @param dataManager 数据管理器
     * @param str
     * @return
     */
    private Result<BigDecimal> getSingleBigDecimal(DataManager dataManager, Object str) {
        Result<BigDecimal> result = new Result<BigDecimal>(true, null);
        
        String val = str == null ? "" : str.toString();
        if (dataManager.containsKey(val)) {
            List list = dataManager.get(val);
            if (list.size() > 1) {
                result.setSuccess(false);
                result.setResult("数据错误，计算需要单个数据，但源数据为集合！");
                return result;
            } else if (list.size() < 1) {
                result.setSuccess(false);
                result.setResult("数据错误，源数据为空！");
                return result;
            }
            Object obj = list.get(0);
            if (obj instanceof Date) {
                Date date = (Date) obj;
                result.setData(new BigDecimal(date.getTime() / 1000 / 3600 / 24));
            } else if (NumericUtils.parseDoubleMute(obj) == null) {
                result.setResult("数据错误，源数据不是有效的数值类型！");
                result.setSuccess(false);
            } else {
                String data = list.get(0).toString();
                result.setData(new BigDecimal(data));
            }
        } else if (NumericUtils.parseDoubleMute(val) != null) {
            result.setData(new BigDecimal(val));
        } else {
            result.setResult("数据错误，源数据缺失（字段=" + val + "）！");
            result.setSuccess(false);
        }

        return result;
    }

    public int dateDiffDay(Date date1, Date date2) {
        long t1 = date1.getTime();
        long t2 = date2.getTime();
        return (int) Math.abs((t1 - t2) / 1000 / 3600 / 24);
    }

    public int dateDiffMonth(Date date1, Date date2) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        int befMonth = calendar.get(Calendar.MONTH);
        int befYear = calendar.get(Calendar.YEAR);
        calendar.setTime(date2);
        int aftMonth = calendar.get(Calendar.MONTH);
        int aftYear = calendar.get(Calendar.YEAR);
        return Math.abs(aftMonth - befMonth) + Math.abs(aftYear - befYear) * 12;
    }

}
