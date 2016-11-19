package HTML;

import com.github.abola.crawler.CrawlerPack;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Student on 2016/9/30.
 */
public class vote {
    public static void main(String[] argv)throws IOException {
        String url = "http://db.cec.gov.tw/histQuery.jsp?voteCode=20160101P1A1&qryType=ctks&prvCode=63&cityCode=000";

        Elements prevPage =
                CrawlerPack.start()
                        .getFromHtml(url)
                        .select(".ctks a:matchesOwn(臺北市..區)");


        ArrayList urlv = new ArrayList();
        for (int i=0; i <=11; i++){
            urlv.add(prevPage.get(i).attr("href"));
        }

//        System.out.println(urlv);

        ArrayList data = new ArrayList();
        int u = 0;
        while(u<urlv.size()) {
            System.out.println(urlv.get(u));

            Elements votec = CrawlerPack.start()
                    .getFromHtml("http://db.cec.gov.tw/" + urlv.get(u))
                    .select("tbody");



            for (Element detail : votec.select("tr:nth-child(2n+2)")) {
                if (!(detail.select("td:eq(0)").text().equals("蔡英文")) & !(detail.select("td:eq(0)").text().equals("宋楚瑜"))) {
                    data.add(detail.select("td:eq(0)").text());
                    data.add(detail.select("td:eq(2)").text());
                    data.add(detail.select("td:eq(3)").text());
                } else {
                    data.add(detail.select("td:eq(1)").text());
                    data.add(detail.select("td:eq(2)").text());
                }
            }
            u++;
        }

//        System.out.println(data);

        int s=0;
        System.out.println("里名,國民黨代號,國民黨得票數,民進黨代號,民進黨得票數,親民黨代號,親民黨得票數,");
        while(s<data.size()) {
            System.out.print(data.get(s)+",");
            s++;
            if(s%7==0){
                System.out.println();
            }
        }
        System.out.println(data.size());

        FileWriter fw = new FileWriter("D:\\vote.csv");
        BufferedWriter bw = new BufferedWriter(fw);
        int m = 0;
        bw.write("里名,國民黨代號,國民黨得票數,民進黨代號,民進黨得票數,親民黨代號,親民黨得票數,");
        bw.newLine();
        while (m < data.size()) {
            bw.write(data.get(m) + ",");
            m++;
            if (m % 7 == 0) {
                bw.newLine();
            }
        }
        bw.flush();
        bw.close();
        fw.close();

    }
}
