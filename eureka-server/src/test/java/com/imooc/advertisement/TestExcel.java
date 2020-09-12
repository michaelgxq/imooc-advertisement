package com.imooc.advertisement;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @ClassName: TestExcel
 * @Description: TODO
 * @author: yourName
 * @date: 2020年06月06日 15:44
 */
public class TestExcel {

    @Ignore
    public void TestCreateExcelJXL() {
        // 创建数组 title，它里面的元素用于设置下面创建的对象 sheet 中的表头名
        String[] title = {"id", "name", "sex"};

        // 1.创建 Excel 文件
        File file = new File("e:/jxl_test.xls");

        try {
//            file.createNewFile();

            // 2.创建 WritableWorkbook 输出流对象 workbook（相当于创建一个工作簿）
            // createWorkbook() 方法中传入上面创建的 File 文件对象
            WritableWorkbook workbook = Workbook.createWorkbook(file);

            // 3.通过对象 workbook 调用 createSheet() 方法创建 WritableSheet 类对象 sheet（即 EXCEL 中的 sheet）
            // createSheet() 方法中传入的第一个形参为创建的这个 sheet 的名字，第二个形参为从这个 sheet 的哪个位置写入数据
            WritableSheet sheet = workbook.createSheet("sheet1", 0);

            // 先创建一个空的 Label 类对象
            Label label = null;

            // 4.往对象 sheet 中写入表头（这里就是循环上面创建的 title 数组，把数组中的元素写入对象 sheet 中作为表头）
            for (int i = 0; i < title.length; i++) {

                // 5.创建 Label 类对象，构造方法中传入的第一个形参表示第几列，第二个参数表示第几行，第三个参数表示要写入的数据
                label = new Label(i, 0, title[i]);

                // 6.通过对象 sheet 调用 addCell() 方法往对象 sheet 中写入数据（相当于往 EXCEL 的单元格中写入数据）
                sheet.addCell(label);
            }

            // 7.追加数据（即重复上面 for 循环中的步骤）
            // 注意这里 i 是从 1 开始的，因为第 0 行已经在上面被用来设置表头了
            for (int i = 1; i < 10; i++) {
                label = new Label(0, i, "a" + i);

                sheet.addCell(label);

                label = new Label(1, i, "user" + i);

                sheet.addCell(label);

                label = new Label(2, i, "男");

                sheet.addCell(label);
            }

            // 8.调用对象 workbook 中的 write() 方法，把数据写入到我们上面创建的 File 类对象所指向的文件中
            workbook.write();

            // 9.调用对象 workbook 中的 close() 方法来关闭输入流
            workbook.close();
        }
        catch (  IOException | WriteException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Ignore
    public void TestReadExcelJXL() {

        try {
            // 1.创建 Workbook 输入流对象 workbook，getWorkbook() 方法中传入指向我们要读取的 EXCEL 文件的 File 类对象
            Workbook workbook = Workbook.getWorkbook(new File("E:/jxl_test.xls"));

            // 2.通过对象 workbook 调用 getSheet() 方法来创建 Sheet 类对象，getSheet() 中传入我们要获取当前这个 EXCEL 文件中的第几个 sheet
            // （此时传入 0 表示获取当前 EXCEL 文件中的第一个 sheet）
            Sheet sheet = workbook.getSheet(0);

            // 3.循环遍历 EXCEL 文件中的每一行，每一列，读取每一个单元格中数据

            // 先通过调用对象 sheet 中的 getRows() 方法获取当前这个 sheet 中有多少行
            for (int i = 0; i < sheet.getRows(); i++) {
                // 再通过调用对象 sheet 中的 getColumns() 方法获取当前这个 sheet 中有多少列
                for (int j = 0; j < sheet.getColumns(); j++) {

                    // 通过调用 sheet 对象中的 getCell() 方法获取 Cell 类对象 cell（即单元格），getCell() 方法中的第一个形参传入的是第几列，第二个形参传入的是第几行
                    Cell cell = sheet.getCell(j, i);

                    // 通过调用对象 cell 中的 getContents() 方法获取单元格中的数据，并输出
                    System.out.print(cell.getContents() + " ");
                }

                System.out.println();
            }


            // 4.通过调用对象 workbook 中的 close() 方法，关闭输入流
            workbook.close();
        }
        catch (IOException | BiffException e) {
            e.printStackTrace();
        }
    }



    @Test
    @Ignore
    public void TestCreateExcelPOI() {
        // 创建数组 title，它里面的元素用于设置下面创建的对象 sheet 中的表头名
        String[] title = {"id", "name", "sex"};

        // 1.创建 HSSFWorkbook 输出流对象（相当于一个工作簿）
        HSSFWorkbook workbook = new HSSFWorkbook();

        // 2.调用 HSSFWorkbook 输出流中的 createSheet() 方法创建 HSSFSheet 类对象（相当于 EXCEL 中的工作表 sheet）
        HSSFSheet sheet = workbook.createSheet();

        // 3.调用 HSSFSheet 类中的 createRow() 方法创建 HSSFRow 类对象（即 EXCEL 中的行），createRow() 方法中传入 0 （即表示第 1 行）
        HSSFRow row = sheet.createRow(0);

        // 先创建一个空的 HSSFCell 类对象
        HSSFCell cell = null;

        for (int i = 0; i < title.length; i++) {

            // 4.调用 HSSFRow 中的 createRow() 方法创建单元格，createCell() 方法中传入的数字表示当前行中第几个单元格
            cell = row.createCell(i);

            // 5.调用 HSSFCell 中的 setCellValue() 方法，往 setCellValue() 方法中传入我们要写入的数据（即相当于往单元格中写入数据）
            cell.setCellValue(title[i]);
        }

        for (int i = 1; i < 10; i++) {
            HSSFRow nextRow = sheet.createRow(i);

            HSSFCell cell1 = nextRow.createCell(0);

            cell1.setCellValue("a" + i);

            cell1 = nextRow.createCell(1);

            cell1.setCellValue("user" + i);

            cell1 = nextRow.createCell(2);

            cell1.setCellValue("男");
        }

        // 6.创建 File 类对象，构造方法中传入我们要创建的 EXCEL 文件的绝对路径
        File file = new File("e:/poi_test.xls");

        try {

            // 7.调用 HSSFWorkbook 输出流中的 write() 方法，往 write() 方法中传入我们上面创建的 File 类对象来往文件中写入数据
            workbook.write(file);

            // 8.调用 HSSFWorkbook 输出流中的 close() 方法来关闭 HSSFWorkbook 输出流
            workbook.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    @Ignore
    public void TestReadExcelPOI() {
        // 1.创建 File 类对象，构造方法中传入我们要读取的 EXCEL 文件的绝对路径
        File file = new File("e:/poi_test.xls");

        try {
            // 2.创建字节输入流 FileInputStream 流对象，构造方法中传入我们上面创建的 File 类对象
            FileInputStream fileInputStream = new FileInputStream(file);

            // 3.创建 HSSFWorkbook 输入流对象，构造方法中传入上面创建的字节输入流 FileInputStream 流对象
            HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);

            // 4.调用 HSSFWorkbook 流中的 getSheet() 方法创建 HSSFSheet 类对象，getSheet() 方法中传入当前我们要获取的工作表（即 sheet）的名称
            HSSFSheet sheet = workbook.getSheetAt(0);

            int firstRowNum = 0;

            // 5.调用 HSSFSheet 中的 getLastRowNum() 获取当前工作表中的最后一行为第几行
            int lastRowNum = sheet.getLastRowNum();

            // 6.循环表里当前工作表中的每一行
            for (int i = 0; i < lastRowNum; i++) {

                // 7.调用 HSSFSheet 类中的 getRow() 方法创建 HSSFRow 类对象，获取当前行的数据
                HSSFRow row = sheet.getRow(i);

                // 8.调用 HSSFRow 中的 getLastCellNum() 方法，获取当前行中最后一个单元格为第几格
                int lastCellNum = row.getLastCellNum();

                // 9.循环遍历当前行中的每一个单元格
                for (int j = 0; j < lastCellNum; j++) {

                    // 10.调用 HSSFCell 类中的 getCell() 方法，获取当前单元格，方法中传入要获取第几个单元格
                    HSSFCell cell = row.getCell(j);

                    // 11.调用 HSSFCell 中的g etStringCellValue() 方法获取当前单元格中的数据
                    String stringCellValue = cell.getStringCellValue();

                    System.out.print(stringCellValue + " ");
                }

                System.out.println();
            }

            // 12.调用 FileInputStream 字节输入流中的 close() 方法关闭字节输入流
            fileInputStream.close();

            // 13.调用 HSSFWorkbook 流中的 close() 方法关闭流
            workbook.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    @Ignore
    public void TestException() {
        // 创建 SAXReader 类对象
        SAXReader saxReader = new SAXReader();

        try {
            // 调用 System 类中的 getProperty() 方法获取当前模块的绝对路径
            String path = System.getProperty("user.dir");

            // 调用 SAXReader 类中的 read() 方法，读取 student.xml 文件
            Document document = saxReader.read(path + "/src/main/resources/student.xml");

            // 创建 HSSFWorkbook 输出流对象（相当于一个工作簿）
            HSSFWorkbook workbook = new HSSFWorkbook();

            // 调用 HSSFWorkbook 输出流中的 createSheet() 方法创建 HSSFSheet 类对象（相当于 EXCEL 中的工作表 sheet）
            HSSFSheet sheet = workbook.createSheet();

            // 调用 Document 类中的 getRootElement() 方法获取我们所读取的 student.xml 文件中的根节点（即此文件中的 excel 标签）
            Element rootElement = document.getRootElement();

            // 调用 Element 类中的 attributeValue() 方法获取根标签中的 name 属性值（此属性值设置的即为当前导入模板的名称）
            String templateName = rootElement.attributeValue("name");

            // 定义行号
            int rowNum = 0;

            // 定义列号
            int columnNum = 0;

            // 首先
            // 我们要设置 EXCEL 表中每一列的宽度

            // 调用 DOM4J 中的 Element 类中的 element() 方法获取我们所读取的 student.xml 文件中的 colgroup 标签
            Element colgroup = rootElement.element("colgroup");

            // 调用下面定义的 setColumnWidth() 方法设置单元格的宽度（即列宽）
            setColumnWidth(sheet, colgroup);

            // 然后
            // 我们要设置 EXCEL 表的标题

            // 调用 DOM4J 中的 Element 类中的 element() 方法获取我们所读取的 student.xml 文件中的 title 标签
            Element title = rootElement.element("title");

            // 调用 DOM4J 中的 Element 类中的 elements() 方法获取我们所读取的 student.xml 文件中的 title 标签中的所有 tr 标签
            List<Element> trs = title.elements("tr");

            // 循环遍历所有的 tr 标签
            for (int i = 0; i < trs.size(); i++) {
                // 获取集合 trs 中的每一个元素
                Element tr = trs.get(i);

                // 调用 DOM4J 中的 Element 类中的 elements() 方法获取我们所读取的 student.xml 文件中的 tr 标签中的所有 td 标签
                List<Element> tds = tr.elements("td");

                // 调用 HSSFSheet 类中的 createRow() 方法，为当前的 EXCEL 表的 sheet 创建行
                HSSFRow row = sheet.createRow(rowNum);

                

                // 循环遍历所有的 td 标签
                for (int j = 0; j < tds.size() ; j++) {
                    // 获取集合 tds 中的每一个元素
                    Element td = tds.get(j);

                    // 调用 HSSFCell 类中的 createCell() 方法创建一个单元格
                    HSSFCell cell = row.createCell(j);

                    // 获取当前 td 标签中的 rowspan 属性（即用 DOM4J 中的 Attribute 类对象来接收）
                    Attribute rowspan = td.attribute("rowspan");

                    // 获取当前 td 标签中的 colspan 属性（即用 DOM4J 中的 Attribute 类对象来接收）
                    Attribute colspan = td.attribute("colspan");

                    // 获取当前 td 标签中的 value 属性（即用 DOM4J 中的 Attribute 类对象来接收）
                    Attribute value = td.attribute("value");

                    // 如果 value 属性不为空
                    if (value != null) {
                        // 获取 value 属性的值
                        String val = value.getValue();


                        // 获取上面 rowspan 属性的值，并把它转为 int 类型
                        // ( 这里 - 1 是因为在 student.xml 中 rowspan 属性的值为 1，但是 EXCEL 中 行 是从 0 开始计算的，所以这里要 -1 )
                        int rspan = Integer.parseInt(rowspan.getValue()) - 1;


                        // 获取上面 colspan 属性的值，并把它转为 int 类型
                        // ( 这里 - 1 是因为在 student.xml 中 rowspan 属性的值为 6，但是 EXCEL 中 行 是从 0 开始计算的，所以这里要在原有的属性值基础上 -1 )
                        int cspan = Integer.parseInt(colspan.getValue()) -1;


                        // 调用 HSSFCell 类中的 setCellValue() 方法，设置当前单元格中的内容
                        cell.setCellValue(val);


                        // 调用 HSSFSheet 类中的 addMergedRegion() 方法来合并单元格
                        // 并且往 addMergedRegion() 方法中传入 CellRangeAddress 类对象来设置要合并的单元格的范围（即起始行，结束行，起始列，结束列）
                        // 即
                        // CellRangeAddress 类的构造方法从传入的分别是 起始行，结束行，起始列，结束列
                        sheet.addMergedRegion(new CellRangeAddress(rspan, rspan, 0, cspan));
                    }
                }

                // 遍历完一行数据（即遍历完当前行的所有 td 标签），之后，行数要加 1
                rowNum++;
            }

            // 设置表头（即设置每一列的列名）

            // 调用 DOM4J 中的 Element 类中的 element() 方法获取我们所读取的 student.xml 文件中的 thead 标签
            Element thead = rootElement.element("thead");

            // 调用 DOM4J 中的 Element 类中的 elements() 方法获取我们所读取的 student.xml 文件中的 thead 标签中所有 tr 标签
            List<Element> trElements = thead.elements("tr");


            // 遍历集合 trElements 中的所有的 tr 标签
            for (int i = 0; i < trElements.size(); i++) {

                // 获取当前所遍历到的 tr 标签
                Element element = trElements.get(i);

                // 调用 HSSFSheet 类中的 createRow() 方法，为当前的 EXCEL 表的 sheet 创建新的一行
                HSSFRow row = sheet.createRow(rowNum);

                // 获取当前 tr 标签中的所有 th 标签
                List<Element> ths = element.elements("th");

                // 遍历集合 ths 中的所有 th 标签
                for (int j = 0; j < ths.size(); j++) {

                    // 获取当前所遍历到的 th 标签
                    Element th = ths.get(j);

                    // 获取当前 th 标签中的 value 属性（即用 DOM4J 中的 Attribute 类对象来接收）
                    Attribute value = th.attribute("value");

                    // 调用 HSSFCell 类中的 createCell() 方法创建一个单元格
                    HSSFCell cell = row.createCell(j);

                    // 判断上面获取到的 value 属性不为空
                    if (value != null) {
                        // 获取当前 value 属性中的值
                        String valueAttr = value.getValue();

                        // 为当前创建的一个单元格设置单元格中的值（即设置在 XML 文件中, th 标签中 value 属性的值为 列名）
                        cell.setCellValue(valueAttr);

                    }
                }

                // 遍历完一行数据（即遍历完当前行的所有 th 标签），之后，行数要加 1
                rowNum++;
            }

            // 设置数据区域的样式（EXCEL 表中正在存放数据的单元格）

            // 调用 DOM4J 中的 Element 类中的 element() 方法获取我们所读取的 student.xml 文件中的 tbody 标签
            Element tbody = rootElement.element("tbody");

            // 调用 DOM4J 中的 Element 类中的 element() 方法获取我们所读取的 student.xml 文件中的 tbody 标签中的 tr 标签
            Element tr = tbody.element("tr");

            // 获取上面获取的 tr 标签中的 repeat 属性
            int repeat = Integer.parseInt(tr.attribute("repeat").getValue());

            // 获取上面获取的 tr 标签中所有的 td 标签
            List<Element> tds = tr.elements("td");

            // 此 for 循环用于循环创建 5 行数据（即 EXCEL 表中的 5 行数据）
            for (int i = 0; i < repeat; i++) {

                // 调用 HSSFSheet 类中的 createRow() 方法，为当前的 EXCEL 表的 sheet 创建新的一行
                // 这里 createRow() 方法中传入的是 rowNum 而不是此时用于循环的变量 i 是因为我们此时新创建的行是要接着上面创建的行所在行数往下创建
                // 如果这里是用变量 i，那么就相当于又从 0 开始新建行，这样上面代码中新建的行就会被覆盖了
                // 并且
                // 执行完此语句后，都要让 rowNum 加 1 以便下次循环到此语句时，再次向下创建一行
                HSSFRow row = sheet.createRow(rowNum++);

                // 循环遍历上面获取到的 tds 集合
                // 此循环用于为当前新建的行依次创建单元格（单元格个数以上面获取的 tds 集合的大小为准（其实就是以 student.xml 文件中，tbody 标签中的 td 标签个数为准））
                for (int j = 0; j < tds.size(); j++) {

                    // 获取当前所遍历到的 td 标签
                    Element td = tds.get(j);

                    // 调用 HSSFCell 类中的 createCell() 方法创建一个单元格
                    HSSFCell cell = row.createCell(j);

                    // 调用下面定义的 setType() 方法设置单元格的数据类型和样式等
                    setType(workbook, cell, td);
                }
            }

            // 创建用于保存我们创建的 EXCEL 模板的 File 类对象
            File file = new File("e:/" + templateName + ".xls");

            // 如果此 File 类对象所指向的路径中有此文件，就先调用 File 类中的 delete() 方法删除
            file.delete();

            // 调用 File 类中的 createNewFile() 方法创建新的文件
            file.createNewFile();

            // 创建字节输出流
            FileOutputStream stream = new FileOutputStream(file);

            // 调用 HSSFWorkbook 类中的 write() 方法，把当前创建的 EXCEL 文件写入到字节输出流中
            workbook.write(stream);

            // 关闭字节输出流
            stream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置列宽
     * @param sheet
     * @param colgroup
     */
    private void setColumnWidth(HSSFSheet sheet, Element colgroup) {

        // 获取形参 colgroup 所接收的标签中的所有子标签
        List<Element> elements = colgroup.elements();


        for (int i = 0; i <elements.size(); i ++) {

            // 获取集合 elements 中的元素
            Element element = elements.get(i);

            // 遍历获取 elements 集合对象中的每一个元素
            // 获取 elements 集合对象中每一个元素的 width 属性值
            String width = element.attributeValue("width");

            // 通过调用 String 类中的 replaceAll() 方法，获取 width 属性值中的单位（即此时的 em）,（这里用到了正则表达式）
            String unit = width.replaceAll("[0-9,.]", "");

            // 通过调用 String 类中的 replaceAll() 方法，获取 width 属性值中的数值（即此方法用于去掉变量 width 所接收的字符串中的 em 这个字符串）
            String value = width.replace(unit, "");

            // 变量 v 用于设置单元格的宽度
            int v = 0;

            // 判断变量 unit 是否为空（即我们在编写 EXCEL 模板 XML 文件时，没有设置单元格宽度的单位）或者 变量 unit 的值为 px（即我们设置单元格的宽度单位为 px）
            if (StringUtils.isBlank(unit) || "px".endsWith(unit)) {

                // 设置单元格的宽度（下面是对于单位是 px 时的固定计算公式）
                 v = Math.round(Float.parseFloat(value) * 37F);


            }
            else if ("em".endsWith(unit)) {// 如果变量 unit 的值为 em（即我们设置单元格的宽度单位为 em）

                // 设置单元格的宽度（下面是对于单位是 em 时的固定计算公式）
                v = Math.round(Float.parseFloat(value) * 267.5F);
            }

                // 通过 HSSFSheet 类对象 sheet 调用 setColumnWidth() 方法设置单元格的宽
                sheet.setColumnWidth(i , v);
        }


    }


    /**
     * 设置单元格样式
     * @param workbook
     * @param cell
     * @param td
     */
    private void setType(HSSFWorkbook workbook, HSSFCell cell, Element td) {

        // 获取形参 td 获取的 Element 类对象中的 type 属性（即用 DOM4J 中的 Attribute 类对象来接收）
        Attribute type = td.attribute("type");

        // 获取上面获取到的 type 属性的属性值
        String value = type.getValue();

        // 调用形参 workbook 所接收的 HSSFWorkbook 类对象中的 createDataFormat() 方法来获取 HSSFDataFormat 类对象用于设置单元格的数据格式
        HSSFDataFormat dataFormat = workbook.createDataFormat();

        // 调用形参 workbook 所接收的 HSSFWorkbook 类对象中的 createCellStyle() 方法来创建 HSSFCellStyle 类对象用于设置单元格的样式
        HSSFCellStyle cellStyle = workbook.createCellStyle();

        // 如果当前上面形参 td 接收到的 Element 类对象（此对象其实就是对应的 td 标签）中的 type 属性值为 NUMERIC
        if ("NUMERIC".equalsIgnoreCase(value)) {

            // 调用 HSSFCell 类中的 setCellType() 方法，把当前单元格的数据类型设置成 NUMERIC
            cell.setCellType(CellType.NUMERIC);

            // 调用 DOM4J 中的 attribute() 方法，获取当前上面形参 td 接收到的 Element 类对象（此对象其实就是对应的 td 标签）中的 format 属性
            Attribute format = td.attribute("format");

            // 获取 format 属性的值
            String value1 = format.getValue();

            // 判断 format 属性值是否为空，如果为空就给它一个默认值 "#,##0.00"
            value1 = StringUtils.isNotBlank(value1) ? value1 : "#,##0.00";

            // 调用 HSSFCellStyle 中的 setDataFormat() 方法来设置单元格的格式
            // 方法中传入通过调用 HSSFDataFormat 类中的 getFormat() 方法获取到的值
            cellStyle.setDataFormat(dataFormat.getFormat(value1));

        }
        // 如果当前上面形参 td 接收到的 Element 类对象（此对象其实就是对应的 td 标签）中的 type 属性值为 STRING
        else if ("STRING".equalsIgnoreCase(value)) {
            // 先调用 HSSFCell 类中的 setCellValue() 方法，设置单元格中的内容为空
            cell.setCellValue("");

            // 调用 HSSFCell 类中的 setCellType() 方法，把当前单元格的数据类型设置成 STRING
            cell.setCellType(CellType.STRING);

            // 调用 HSSFCellStyle 中的 setDataFormat() 方法来设置单元格的格式
            // 方法中传入通过调用 HSSFDataFormat 类中的 getFormat() 方法获取到的值
            // 这里 getFormat() 方法中传入的 @ 即表示字符串类型（待验证）
            cellStyle.setDataFormat(dataFormat.getFormat("@"));
        }
        // 如果当前上面形参 td 接收到的 Element 类对象（此对象其实就是对应的 td 标签）中的 type 属性值为 ENUM
        else if ("ENUM".equalsIgnoreCase(value)) {
            // 由于是枚举值（即在 EXCEL 中是下拉框的模式）
            // 所以
            // 这里先创建一个 CellRangeAddressList 类对象
            // 构造方法中传入的分别是当前单元格所在的行数和列数
            CellRangeAddressList regions = new CellRangeAddressList(cell.getRowIndex(), cell.getRowIndex(), cell.getColumnIndex(), cell.getColumnIndex());

            // 调用 DOM4J 中的 attribute() 方法，获取当前上面形参 td 接收到的 Element 类对象（此对象其实就是对应的 td 标签）中的 format 属性
            Attribute format = td.attribute("format");

            // 获取 format 属性的值
            String value1 = format.getValue();

            // 调用 DVConstraint 类中的 createExplicitListConstraint() 方法
            // 把当前上面形参 td 接收到的 Element 类对象（此对象其实就是对应的 td 标签）中的 format 属性的值设置成 EXCEL 下拉框中的值
            // 由于
            // 此时 format 属性的值是字符串（即我们在 student.xml 中对应 td 标签的 format 属性中设置的 “男,女”）
            // 而 createExplicitListConstraint() 方法接收的数组
            // 所以
            // 我们这里调用 String 类的 split() 方法把这个字符串用 ，号分割成数组
            DVConstraint explicitListConstraint = DVConstraint.createExplicitListConstraint(value1.split(","));

            // 创建数据有效性对象 HSSFDataValidation 类对象
            // 构造方法中传入上面创建的对象 regions 和 对象 explicitListConstraint
            HSSFDataValidation hssfDataValidation = new HSSFDataValidation(regions, explicitListConstraint);

            // 调用 HSSFWorkbook 类中的 getSheetAt() 方法获取当前 EXCEL 中的 sheet 对象
            // 然后
            // 在调用 sheet 对象中的 addValidationData() 方法，把上面创建的数据有效性对象设置到当前的 EXCEL 的 sheet 中
            workbook.getSheetAt(0).addValidationData(hssfDataValidation);
        }
        // 如果当前上面形参 td 接收到的 Element 类对象（此对象其实就是对应的 td 标签）中的 type 属性值为 DATE
        else if ("DATE".equalsIgnoreCase(value)) {
            // 调用 HSSFCell 类中的 setCellType() 方法，把当前单元格的数据类型设置成 NUMERIC
            cell.setCellType(CellType.NUMERIC);

            // 调用 HSSFCellStyle 中的 setDataFormat() 方法来设置单元格的格式
            // 方法中传入通过调用 HSSFDataFormat 类中的 getFormat() 方法获取到的值
            // 这里 getFormat() 方法中传入显示日期的格式
            cellStyle.setDataFormat(dataFormat.getFormat("yyyy-mm-dd"));

        }

        // 调用 HSSFCell 类中的 setCellStyle() 方法设置单元格的样式
        cell.setCellStyle(cellStyle);
    }


}


