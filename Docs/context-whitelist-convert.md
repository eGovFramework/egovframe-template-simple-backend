# context-whitelist.xml  설정 변환

> white list를 관리하는 설정이다.



white list를 등록하는 설정이다.

<context-whitelist.xml>

```xml
<util:list id="egovPageLinkWhitelist" value-type="java.lang.String">
    <value>main/inc/EgovIncHeader</value>
    <value>main/inc/EgovIncTopnav</value>
    <value>main/inc/EgovIncLeftmenu</value>
    <value>main/inc/EgovIncFooter</value>
    <value>main/sample_menu/Intro</value>
    <value>main/sample_menu/EgovDownloadDetail</value>
    <value>main/sample_menu/EgovDownloadModify</value>
    <value>main/sample_menu/EgovQADetail</value>
    <value>main/sample_menu/EgovServiceInfo</value>
    <value>main/sample_menu/EgovAboutSite</value>
    <value>main/sample_menu/EgovHistory</value>
    <value>main/sample_menu/EgovOrganization</value>
    <value>main/sample_menu/EgovLocation</value>
    <value>main/sample_menu/EgovProductInfo</value>
    <value>main/sample_menu/EgovProductInfo</value>
    <value>main/sample_menu/EgovServiceInfo</value>
    <value>main/sample_menu/EgovDownload</value>
    <value>main/sample_menu/EgovQA</value>
    <value>main/sample_menu/EgovService</value>
</util:list>
```

<EgovConfigAppWhitelist.class>

```java
@Bean
	public List<String> egovPageLinkWhitelist() {
		List<String> whiteList = new ArrayList<String>();
		whiteList.add("main/inc/EgovIncHeader");
		whiteList.add("main/inc/EgovIncTopnav");
		whiteList.add("main/inc/EgovIncLeftmenu");
		whiteList.add("main/inc/EgovIncFooter");
		whiteList.add("main/sample_menu/Intro");
		whiteList.add("main/sample_menu/EgovDownloadDetail");
		whiteList.add("main/sample_menu/EgovDownloadModify");
		whiteList.add("main/sample_menu/EgovQADetail");
		whiteList.add("main/sample_menu/EgovAboutSite");
		whiteList.add("main/sample_menu/EgovHistory");
		whiteList.add("main/sample_menu/EgovOrganization");
		whiteList.add("main/sample_menu/EgovLocation");
		whiteList.add("main/sample_menu/EgovProductInfo");
		whiteList.add("main/sample_menu/EgovServiceInfo");
		whiteList.add("main/sample_menu/EgovDownload");
		whiteList.add("main/sample_menu/EgovQA");
		whiteList.add("main/sample_menu/EgovService");
		return whiteList;
	}
```