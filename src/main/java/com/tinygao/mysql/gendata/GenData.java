package com.tinygao.mysql.gendata;

import com.google.common.base.*;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.tinygao.util.DataTypeFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.tinygao.util.DataTypeFactory.genType;

/**
 * Created by gsd on 2017/3/11.
 */
@Slf4j
public class GenData {
    public final String file = "";
    //public final static String template_user_type_info = "user_type_info,200,1,int-9,int-3,int-10";
    public final static Splitter COMMA_SPLIT = Splitter.on(",").trimResults();
    public final static Splitter MINUS_SPLIT = Splitter.on("-").trimResults();

    public final static ExecutorService fileOperate = Executors.newFixedThreadPool(8);

    public static Template parse(String template) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(template), "The param template is empty or null.");

        List<String> templateList = COMMA_SPLIT.splitToList(template);
        String filename = templateList.get(0);
        long num = Long.parseLong(templateList.get(1));
        long id = Long.parseLong(templateList.get(2));

        Map<Integer, TypeElement> map = Maps.newConcurrentMap();
        for(int i = 3, length = templateList.size(); i < length; i++) {
            List<String> typeList = MINUS_SPLIT.splitToList(templateList.get(i));
            TypeElement tmp = new TypeElement(Type.getTypeByStr(typeList.get(0)),  Integer.parseInt(typeList.get(1)));
            map.put(i-1, tmp);
        }
        return new Template(filename, num, id, map);
    }

    public static void genData(String templateStr) throws IOException, InterruptedException {
        Template template = parse(templateStr);

        String path = System.getProperty("java.io.tmpdir");
        String output = path + template.getFilename()+ "_"+template.getNum()+".csv";
        File outputFile = new File(output);
        log.info("save to file : {}", output);
        StringBuffer sb = new StringBuffer(20000);

        if(template.getNum()>1) {
            sb.append(template.getIdFrom()).append("\t");
            for(TypeElement element : template.getValues().values()) {
                sb.append(DataTypeFactory.genType(element.getType()).getRandom(element.getSize()));
                sb.append("\t");
            }
        }

        for(long i = 2 ; i <= template.getNum(); i++) {
            sb.append("\r\n");
            sb.append(template.getIdFrom()+ i-1).append("\t");
            for(TypeElement element : template.getValues().values()) {
                sb.append(DataTypeFactory.genType(element.getType()).getRandom(element.getSize()));
                sb.append("\t");
            }
            if(i % 2000 == 0) {
                appendToFile(outputFile, sb);
            }
        }
        appendToFile(outputFile, sb);
        fileOperate.shutdown();
        fileOperate.awaitTermination(1, TimeUnit.DAYS);
    }

    private static void appendToFile(File outputFile, StringBuffer sb) throws IOException {
        Files.append(sb.toString(), outputFile, Charset.forName("UTF-8"));
        sb.setLength(0);
    }

    public static void main(String[] args) throws IOException {
        //Read template from file
        File file = null;
        if(args.length <=0) {
            log.info("file path : {} ", GenData.class.getClassLoader().getResource("").getPath()+"template.txt");
            file = new File(GenData.class.getClassLoader().getResource("").getPath()+"template.txt");
        }
        else {
            file = new File(args[0]);
        }
        List<String> list = Files.readLines(file, Charset.forName("UTF-8"));
        Stopwatch st = Stopwatch.createStarted();
        list.forEach((ele)->{
            if(ele.startsWith("#")) {
                return;
            }
            else {
                try {
                     genData(ele);
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
        log.info("cost time(ms) : {} .",st.elapsed(TimeUnit.MILLISECONDS));
        st.stop();
    }
}
