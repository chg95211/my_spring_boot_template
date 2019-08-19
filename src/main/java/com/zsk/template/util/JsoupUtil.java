package com.zsk.template.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

/**
 * @description:
 * @author: zsk
 * @create: 2019-08-15 20:30
 **/
public class JsoupUtil
{
    public static void main(String[] args)
    {
        String html = "zsk\n" + "zsk2\n" + "zsk3";
        String text =Jsoup.clean(html, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));

        System.out.println(text);
    }


}
