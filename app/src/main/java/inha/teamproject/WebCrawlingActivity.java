package inha.teamproject;

//09/10 서머스쿨때 구현했던 웹 크롤링 재현중...
/*참고자료들
 * jsoup 파싱 자료
 * https://www.journaldev.com/7207/google-search-from-java-program-example*/

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


public class WebCrawlingActivity extends AppCompatActivity {

    /*TODO
     * htmlPageUrl은 받은 텍스트를 기반으로 검색을 진행한 후 나온 사이트검색결과로 한다(해결)
     * stringsearchactivity_add 로부터 리스트를 받아 해당 리스트의 원소들을 검색 키워드로 사용한다*/

    /*서치 추가코드*/
    public static final String GOOGLE_SEARCH_URL = "https://www.google.com/search";

    private String htmlPageUrl = ""; //파싱할 홈페이지의 URL주소
    private TextView textviewHtmlDocument;
    private String htmlContentInStringFormat="";

    int cnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_crawling);

        textviewHtmlDocument = (TextView)findViewById(R.id.crawlingResult);
        textviewHtmlDocument.setMovementMethod(new ScrollingMovementMethod());

        //버튼 지우고 창 로드하면 바로 실행되도록 변경
        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();
        /*이전코드
        Button htmlTitleButton = (Button)findViewById(R.id.button);
        htmlTitleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println( (cnt+1) +"번째 파싱");
                JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
                jsoupAsyncTask.execute();
                cnt++;
            }
        });*/

    }

    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /*파싱한 텍스트를 htmlContentInStringFormat 스트링에 추가
         * ERROR runtimeexception permission denied*/
        @Override
        protected Void doInBackground(Void... params) {
            try {
                String searchTerm = "hanbit";              //여기에다 검색할 키워드 조합을 집어넣습니다
                int num = 5;                            //검색결과갯수
                htmlPageUrl = GOOGLE_SEARCH_URL + "?q=" + searchTerm+"&num="+num;
                Document doc = Jsoup.connect(htmlPageUrl).userAgent("Mozilla/5.0").get();


                //테스트1
                Elements results = doc.select("h3.r > a");

                System.out.println("-------------------------------------------------------------");
                for (Element result : results) {
                    String linkHref = result.attr("href");
                    String linkText = result.text();
                    /*테스트용
                    System.out.println("Text: " + linkText + "," +
                            "URL::" + linkHref.substring(6, linkHref.indexOf("&")));
                            */
                    htmlContentInStringFormat += linkText + "\n" +
                            linkHref.substring(6, linkHref.indexOf("&")) + "\n";//이전코드 e.text().trim() + "\n";
                }

            } catch (IOException e) {
                e.printStackTrace();
                htmlContentInStringFormat += e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            textviewHtmlDocument.setText(htmlContentInStringFormat);
        }
    }
}