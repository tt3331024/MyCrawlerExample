package HTML;
import com.github.abola.crawler.CrawlerPack;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by tt on 2016/7/16.
 * 爬取HTML table形式的資料
 */
public class IncomeByTaipeiVillage {
    public static void main(String[] args) throws IOException{

        int i = 100;
        while(i<=103) {
            String uri = "http://www.fia.gov.tw/public/Attachment/isa" + i + "/" + i + "_165-A.html";
            //取出網站中有用的表格資料
            Elements transDetail =
                    CrawlerPack.start()
                            .getFromHtml(uri)
                            .select("tbody:contains(臺 北 市)");

            //取出表格年度
//        String day = transDetail.select("span:matchesOwn(年度)").text();
            // 取出第五行後的所有交易資料(前四行為沒用的資訊)
            ArrayList data = new ArrayList();
            for (Element detail : transDetail.select("tr:gt(4)")) {
                //將每一格的內容取出，放入ArrayList中
                int j = 1;
                while (j <= 10) {
                    data.add(detail.select("td:eq(" + j + ")").text());
                    j++;
                }
            }
            //刪去空白沒有資料的行列
            for (int n = 1; n <= data.size(); n = n + 10) {
                if (data.get(n).equals("")) {
                    data.remove(n + 8);
                    data.remove(n + 7);
                    data.remove(n + 6);
                    data.remove(n + 5);
                    data.remove(n + 4);
                    data.remove(n + 3);
                    data.remove(n + 2);
                    data.remove(n + 1);
                    data.remove(n);
                    data.remove(n - 1);
                    n = n - 10; //刪除表格後，後面的欄位會向前補上，因此n需要-10，否則會跳行
                }
            }
            //補上村里的行政區資訊
            for (int n = 10; n < data.size(); n = n + 10) {
                if (data.get(n).equals("")) {
                    data.set(n, data.get(n - 10));
                }
            }
//        int m=0;
//        while(m<data.size()) {
//            System.out.print(data.get(m)+",");
//            m++;
//            if(m%10==0){
//                System.out.println();
//            }
//        }
            FileWriter fw = new FileWriter("D:\\IncomeByTaipeiVillage_"+i+".csv");
            BufferedWriter bw = new BufferedWriter(fw);
            int m = 0;
            while (m < data.size()) {
                bw.write(data.get(m) + ",");
                m++;
                if (m % 10 == 0) {
                    bw.newLine();
                }
            }
            bw.flush();
            bw.close();
            fw.close();
            i++;
        }
    }
}
