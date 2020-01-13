package com.icss.mvp.entity;

public class MenuAuth {
	
		private static final long serialVersionUID = 1L;

		private String authId;

		private String authName;

		private String menuUrl;

		private String parentId;

		private String isMenu;

		private Integer ignoe;

		private String open;

		private String level;

		public String getAuthId() {
			return authId;
		}

		public void setAuthId(String authId) {
			this.authId = authId;
		}

		public String getAuthName() {
			return authName;
		}

		public void setAuthName(String authName) {
			this.authName = authName;
		}

		public String getMenuUrl() {
			return menuUrl;
		}

		public void setMenuUrl(String menuUrl) {
			this.menuUrl = menuUrl;
		}

		public String getParentId() {
			return parentId;
		}

		public void setParentId(String parentId) {
			this.parentId = parentId;
		}

		public String getIsMenu() {
			return isMenu;
		}

		public void setIsMenu(String isMenu) {
			this.isMenu = isMenu;
		}

		public Integer getIgnoe() {
			return ignoe;
		}

		public void setIgnoe(Integer ignoe) {
			this.ignoe = ignoe;
		}

		public String getOpen() {
			return open;
		}

		public void setOpen(String open) {
			this.open = open;
		}

		public String getLevel() {
			return level;
		}

		public void setLevel(String level) {
			this.level = level;
		}
}
