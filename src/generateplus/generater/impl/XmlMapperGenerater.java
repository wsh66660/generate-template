package generateplus.generater.impl;

import java.util.ArrayList;
import java.util.List;
import generateplus.domain.DataMeta;
import generateplus.generater.AbstractGenerater;
import util.CommentUtil;

/**
 * GenerateXmlMapper
 * 
 * @author wangsihong@hztianque.com
 * @date 2018年10月26日 下午1:44:21
 */
public class XmlMapperGenerater extends AbstractGenerater {

  @Override
  protected void setBasicData() {
    basicData.setFolderName("mapper");
    basicData.setSuffix(basicData.getUpperCaseDomainName() + "Mapper.xml");
  }

  @Override
  public String building() {
    StringBuilder sb = new StringBuilder();
    sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n\n");

    sb.append(
        "<!DOCTYPE mapper  PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"  \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n\n");

    String namespace = String.format("%s.mapper.%sMapper", basicData.getClassPath(),
        basicData.getUpperCaseDomainName());
    sb.append("<mapper namespace=\"").append(namespace).append("\">\n");
    sb.append("\t<resultMap id=\"").append(basicData.getLowerCaseDomainName())
        .append("Result\" type=\"").append(basicData.getClassPath()).append(".domain.")
        .append(basicData.getUpperCaseDomainName()).append("\">\n");
    for (String column : basicData.getColumns()) {
      sb.append("\t\t<result property=\"" + CommentUtil.replaceUnderlineAndfirstToUpper(column)
          + "\" column=\"" + column + "\"/>\n");
    }
    sb.append("\t</resultMap>\n\n");
    sb.append("\t<sql id=\"columns\">\n");
    sb.append("\t\t" + String.join(",", basicData.getColumns()) + "\n");
    sb.append("\t</sql>\n\n");

    // add
    sb.append("\t<insert id=\"add" + basicData.getUpperCaseDomainName() + "\" parameterType=\""
        + basicData.getClassPath() + ".domain." + basicData.getUpperCaseDomainName() + "\"\n");
    sb.append("\t\tuseGeneratedKeys=\"true\" keyProperty=\"id\">\n");
    sb.append("\t\tINSERT INTO " + basicData.getTableName() + "\n");
    List<String> list = new ArrayList<String>();
    for (String column : basicData.getColumns()) {
      if (!"id".equals(column)) {
        list.add(column);
      }
    }
    sb.append("\t\t(" + String.join(",", list) + ")\n");
    sb.append("\t\tVALUES\n");
    list = new ArrayList<String>();
    for (String field : basicData.getFields()) {
      if (!"id".equals(field)) {
        list.add(field);
      }
    }
    sb.append("\t\t(#{" + String.join("},#{", list) + "})\n");
    sb.append("\t</insert>\n\n");

    // getById
    sb.append("\t<select id=\"get" + basicData.getUpperCaseDomainName()
        + "ById\" parameterType=\"java.lang.Long\" resultMap=\""
        + basicData.getLowerCaseDomainName() + "Result\">\n");
    sb.append("\t\tSELECT\n");
    sb.append("\t\t<include refid=\"columns\" />\n");
    sb.append("\t\tFROM " + basicData.getTableName() + " WHERE id=#{value}\n");
    sb.append("\t</select>\n\n");

    // deleteByIds
    sb.append("\t<delete id=\"delete" + basicData.getUpperCaseDomainName() + "ByIds\">\n");
    sb.append("\t\tDELETE FROM " + basicData.getTableName() + " WHERE id IN\n");
    sb.append("\t\t<foreach collection=\"array\" item=\"item\" open=\"(\" separator=\",\"\n");
    sb.append("\t\t\tclose=\")\">\n");
    sb.append("\t\t\t#{item}\n");
    sb.append("\t\t</foreach>\n");
    sb.append("\t</delete>\n\n");

    // update
    sb.append("\t<update id=\"update" + basicData.getUpperCaseDomainName() + "\" parameterType=\""
        + basicData.getClassPath() + ".domain." + basicData.getUpperCaseDomainName() + "\">\n");
    sb.append("\tUPDATE " + basicData.getTableName() + "\n");
    sb.append("\t\t<set>\n");


    List<DataMeta> dataMetaList = basicData.getDataMetaList();
    for (int i = 0; i < dataMetaList.size(); i++) {
      DataMeta dataMeta = dataMetaList.get(i);
      String column = dataMeta.getColumnName();
      String field = basicData.getFields().get(i);
      String fieldType = basicData.getFieldTypes().get(i);

      if ("id".equals(column)) {
        continue;
      }
      if ("String".equals(fieldType)) {
        sb.append("\t\t\t<if test=\"" + field + " != null and " + field + " != ''\"> \n");
      } else {
        sb.append("\t\t\t<if test=\"" + field + " != null\"> \n");
      }
      sb.append("\t\t\t\t" + column + " = #{" + field + "},\n");
      sb.append("\t\t\t</if>\n");

    }
    sb.append("\t\t</set>\n");
    sb.append("\t\tWHERE id=#{id}\n");
    sb.append("\t</update>\n\n");

    // findForList
    sb.append("\t<select id=\"find" + basicData.getUpperCaseDomainName() + "ForList\" resultMap=\""
        + basicData.getLowerCaseDomainName() + "Result\"\n");
    sb.append("\t\tparameterType=\"" + basicData.getClassPath() + ".domain."
        + basicData.getUpperCaseDomainName() + "\">\n");
    sb.append("\t\tSELECT\n");
    sb.append("\t\t<include refid=\"columns\" />\n");
    sb.append("\t\tFROM " + basicData.getTableName() + "\n");
    sb.append("\t\t<where>\n");

    dataMetaList = basicData.getDataMetaList();
    for (int i = 0; i < dataMetaList.size(); i++) {
      DataMeta dataMeta = basicData.getDataMetaList().get(i);
      String column = dataMeta.getColumnName();
      String field = basicData.getFields().get(i);
      String fieldType = basicData.getFieldTypes().get(i);

      if ("String".equals(fieldType)) {
        sb.append("\t\t\t<if test=\"" + field + " != null and " + field + " != ''\">\n");
        sb.append("\t\t\t\tand " + column + " = #{" + field + "}\n");
        sb.append("\t\t\t</if>\n");
      } else {
        sb.append("\t\t\t<if test=\"" + field + " != null \">\n");
        sb.append("\t\t\t\tand " + column + " = #{" + field + "}\n");
        sb.append("\t\t\t</if>\n");
      }
    }

    sb.append("\t\t</where>\n");
    sb.append("\t</select>\n\n");
    sb.append("</mapper>\n");
    return sb.toString();
  }
}
