var projNo = getCookie("projNo_comm");
$(function () {
    $('.cute-content').delegate('.kpi-line .kpi-name', 'click', function () {
        var checkbox = $(this).prev();
        var checked = !checkbox.prop("checked");
        checkbox.prop("checked", checked).trigger('change');
    });
    $('.cute-content').delegate('.kpi-checkbox', 'change', function () {
        var checkbox = $(this);
        var checked = checkbox.prop("checked");
        var text = checkbox.parents(".kpi-line").find(".description");
        if (checked == true) {
            checkbox.next("label").removeClass("excluded-label");
            text.attr('disabled', 'disabled').val("");
        } else {
            checkbox.next("label").addClass("excluded-label");
            text.removeAttr('disabled')
        }
    });

    $("#btn_reset").click(function () {
        $('form')[0].reset();
        $('form').find('input[type="checkbox"]:not(:checked)').next().addClass("excluded-label");
        $('form').find('input[type="checkbox"]:not(:checked)').parents(".kpi-line").find(".description").removeAttr('disabled');
    });
    $("#btn_save").click(function () {
        var texts = $('form').find('input[type="text"]:not(:disabled)');

        for (var i = 0; i < texts.length; i++) {
            var text = $(texts[i]).val();
            if ($.trim(text).length <= 0) {
                toastr.error("请对裁剪项进行说明");
                $(texts[i]).focus();
                return;
            }
        }
        var checkboxs = $('form').find('input[type="checkbox"]:not(:checked)');
        var datas = [];
        checkboxs.each(function () {
            var data = {
                reportConfigId: $(this).attr("reportconfigid"),
                reportKpiConfigId: $(this).attr("reportkpiconfigid"),
                description: $(this).parents(".kpi-line").find(".description").val(),
                projectId: projNo
            }
            datas.push(data);
        });


        $.ajax({
            url: getRootPath() + '/report/report_config_excluded/save',
            data: JSON.stringify({
                projectNo: projNo, excludeds: datas
            }),
            contentType: "application/json",
            method: "post",
            success: function (resp) {
                if (resp.succeed == true) {
                    toastr.success(resp.message);
                } else {
                    toastr.success(resp.message);
                }
            }
        });
    });
    $.ajax({
        url: getRootPath() + '/report/reportConfig/list_full_by_project_no',
        data: {projectNo: projNo},
        success: function (resp) {
            if (resp.succeed == true) {
                initPage(resp.data)
            } else {
                toastr.success(resp.message);
            }
        }
    });
    var loadExcludeds = function (callback) {
        $.ajax({
            url: getRootPath() + '/report/report_config_excluded/list_by_project_no',
            data: {projectNo: projNo},
            success: function (resp) {
                if (resp.succeed == true) {
                    var excludeds = resp.data;
                    var datas = {};
                    for (var i = 0; i < excludeds.length; i++) {
                        var excluded = excludeds[i];
                        datas[excluded.reportConfigId + '_' + excluded.reportKpiConfigId] = excluded.description;
                    }
                    callback(datas);
                } else {
                    toastr.success(resp.message);
                }
            }
        });
    }
    var initPage = function (reportConfigs) {
        loadExcludeds(function (excludeds) {
            for (var i = 0; i < reportConfigs.length; i++) {
                var reportConfig = reportConfigs[i];
                var lines = ['<div class="row" style="font-size: 18px;border-bottom: 1px solid #ccc;font-weight: bold;">' + reportConfig.name + '</div>'];
                lines.push('<div class="row" style="padding-bottom: 20px;">');
                $('.cute-content').append();
                var refs = reportConfig.kpiConfigRefs;
                for (var j = 0; j < refs.length; j++) {
                    var ref = refs[j];
                    var key = ref.reportConfigId + '_' + ref.reportKpiConfigId;
                    var excluded = excludeds.hasOwnProperty(key);
                    lines.push('<div class="row kpi-line">');
                    lines.push('  <div class="col-sm-2 kpi-name-box">');
                    lines.push('     <input type="checkbox" class="kpi-checkbox" ' + (excluded ? '' : 'checked') + '  reportConfigId="' + ref.reportConfigId + '"  reportKpiConfigId="' + ref.reportKpiConfigId + '"/><label class="kpi-name ' + (excluded ? 'excluded-label' : '') + '">' + ref.reportKpiConfig.kpiName + '</label>');
                    lines.push('  </div>');
                    lines.push('  <div class="col-sm-8">');
                    lines.push('     <input type="text" class="description" ' + (excluded ? '' : 'disabled="disabled"') + 'value="' + (excludeds[key] == undefined ? "" : excludeds[key]) + '">');
                    lines.push('  </div>');
                    lines.push('</div>');
                }
                lines.push('</div>');
                $('form').append(lines.join("\n"));
            }
        });
    }
})

