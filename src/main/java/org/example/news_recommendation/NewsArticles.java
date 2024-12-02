package org.example.news_recommendation;

public class NewsArticles {

    private String articletitle;

    private String articlecontent;

    public NewsArticles (String articletitle,String articlecontent){

        this.articletitle = articletitle;
        this.articlecontent = articlecontent;

    }

    public String getArticletitle() {return articletitle;}

    public String getArticlecontent(){return articlecontent;}

}
