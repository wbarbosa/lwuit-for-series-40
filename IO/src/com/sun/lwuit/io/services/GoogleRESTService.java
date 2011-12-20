/*
 * Copyright (c) 2008, 2010, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores
 * CA 94065 USA or visit www.oracle.com if you need additional information or
 * have any questions.
 */

package com.sun.lwuit.io.services;

import com.sun.lwuit.io.ConnectionRequest;
import com.sun.lwuit.io.NetworkEvent;
import com.sun.lwuit.io.util.JSONParseCallback;
import com.sun.lwuit.io.util.JSONParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

/**
 * Uses the google REST search result API and parses the results for manipulation
 * by an application. http://code.google.com/apis/ajaxsearch/
 * Returns via the action event a vector containing entries of GoogleRESTService.ResultEntry.
 * Please make sure to abide by the terms of use http://code.google.com/apis/ajaxsearch/terms.html and
 * obtain an API key http://code.google.com/apis/ajaxsearch/key.html
 *
 * @author Shai Almog
 */
public class GoogleRESTService extends ConnectionRequest implements JSONParseCallback {
    private Vector results;
    private ResultEntry currentEntry;
    private static String defaultAPIKey;

    /**
     * Constant for the request type in the constructor
     */
    public static final String WEB_SEARCH = "web";

    /**
     * Constant for the request type in the constructor
     */
    public static final String LOCAL_SEARCH = "local";

    /**
     * Constant for the request type in the constructor
     */
    public static final String VIDEO_SEARCH = "video";

    /**
     * Constant for the request type in the constructor
     */
    public static final String BLOG_SEARCH = "blogs";

    /**
     * Constant for the request type in the constructor
     */
    public static final String NEWS_SEARCH = "news";

    /**
     * Constant for the request type in the constructor
     */
    public static final String BOOK_SEARCH = "books";

    /**
     * Constant for the request type in the constructor
     */
    public static final String IMAGE_SEARCH = "images";

    /**
     * Constant for the request type in the constructor
     */
    public static final String PATENT_SEARCH = "patent";

    /**
     * Constant for the safe search method
     */
    public static final String SAFE_SEARCH_ACTIVE = "active";

    /**
     * Constant for the safe search method
     */
    public static final String SAFE_SEARCH_MODERATE = "moderate";

    /**
     * Constant for the safe search method
     */
    public static final String SAFE_SEARCH_OFF = "off";

    /**
     * The constructor accepts the search query to send to google defaulting to web search
     *
     * @param query the query sent to google
     */
    public GoogleRESTService(String query) {
        this(query, WEB_SEARCH);
    }

    /**
     * The constructor accepts the search query to send to google
     *
     * @param query the query sent to google
     * @param requestType one of the constant search values defined e.g. WEB_SEARCH, VIDEO_SEARCH, BLOG_SEARCH, LOCAL_SEARCH etc.
     */
    public GoogleRESTService(String query, String requestType) {
        setPost(false);
        setUrl("http://ajax.googleapis.com/ajax/services/search/" + requestType);
        addArgument("v", "1.0");
        addArgument("q", query);
        if(defaultAPIKey != null) {
            addArgument("key", defaultAPIKey);
        }
    }

    /**
     * Sets the number of results to return from the request
     * 
     * @param size a number between 1 and 8
     */
    public void setResultSize(int size) {
        addArgument("rsz", "" + size);
    }

    /**
     * Sets the language for the results
     *
     * @param l a two character language code
     */
    public void setLanguage(String l) {
        addArgument("hl", l);
    }

    /**
     * Cursor position for the next request
     *
     * @param c cursor position
     */
    public void setStartCursor(int c) {
        addArgument("start", "" + c);
    }

    /**
     * Sets the Google API key for this request see http://code.google.com/apis/ajaxsearch/key.html
     * 
     * @param key google API key
     */
    public void setAPIKey(String key) {
        addArgument("key", key);
    }

    /**
     * Determines the safe search value for the image search
     *
     * @param type SAFE_SEARCH_OFF, SAFE_SEARCH_MODERATE, SAFE_SEARCH_ACTIVE
     */
    public void setSafeSearch(String type) {
        addArgument("safe", type);
    }

    /**
     * Indicates the scoring for the results
     * 
     * @param s the score for the results
     */
    public void setScoring(String s) {
        addArgument("scoring", s);
    }

    /**
     * Sets the default Google API key see http://code.google.com/apis/ajaxsearch/key.html
     *
     * @param key google API key
     */
    public static void setDefaultAPIKey(String key) {
        defaultAPIKey = key;
    }

    /**
     * @inheritDoc
     */
    protected void readResponse(InputStream input) throws IOException  {
        InputStreamReader i = new InputStreamReader(input, "UTF-8");
        results = new Vector();
        JSONParser.parse(i, this);
        fireResponseListener(new NetworkEvent(this, results));
    }

    /**
     * Returns the search results
     * @return results
     */
    public Vector getResults() {
        return results;
    }

    /**
     * @inheritDoc
     */
    public void startBlock(String blockName) {
    }

    /**
     * @inheritDoc
     */
    public void endBlock(String blockName) {
        if(currentEntry != null) {
            results.addElement(currentEntry);
            currentEntry = null;
        }
    }

    /**
     * @inheritDoc
     */
    public void startArray(String arrayName) {
    }

    /**
     * @inheritDoc
     */
    public void endArray(String arrayName) {
    }

    /**
     * @inheritDoc
     */
    public void stringToken(String tok) {
    }

    /**
     * @inheritDoc
     */
    public void numericToken(double tok) {
    }

    private ResultEntry getCurrent() {
        if(currentEntry == null) {
            currentEntry = new ResultEntry();
        }
        return currentEntry;
    }

    /**
     * @inheritDoc
     */
    public void keyValue(String key, String value) {
        if(key.equals("url")) {
            getCurrent().setUrl(value);
        }
        if(key.equals("contentNoFormatting")) {
            getCurrent().setContent(value);
        }
        if(key.equals("visibleUrl")) {
            getCurrent().setVisibleUrl(value);
        }
        if(key.equals("titleNoFormatting")) {
            getCurrent().setTitle(value);
        }
        if(key.equals("estimatedResultCount")) {
            getCurrent().setEstimatedResultCount(value);
        }
        if(key.equals("currentPageIndex")) {
            getCurrent().setCurrentPageIndex(value);
        }
        if(key.equals("tbUrl")) {
            getCurrent().setTbUrl(value);
        }
        if(key.equals("staticMapUrl")) {
            getCurrent().setStaticMapUrl(value);
        }
        if(key.equals("tbWidth")) {
            getCurrent().setTbWidth(value);
        }
        if(key.equals("tbHeight")) {
            getCurrent().setTbHeight(value);
        }
    }

     /**
     * @inheritDoc
     */
    public boolean isAlive() {
        return !isKilled();
    }

    /**
     * Class representing the results from the request, for details of the various
     * attribute values please follow the class references at:
     * http://code.google.com/apis/ajaxsearch/documentation/reference.html#_restUrlBase
     */
    public static class ResultEntry {
        private String url;
        private String title;
        private String visibleUrl;
        private String content;
        private String estimatedResultCount;
        private String currentPageIndex;
        private String tbUrl;
        private String staticMapUrl;
        private String tbWidth;
        private String tbHeight;

        /**
         * Prevent manual creation of this class
         */
        ResultEntry() {}

        /**
         * @return the url
         */
        public String getUrl() {
            return url;
        }

        /**
         * @param url the url to set
         */
        void setUrl(String url) {
            this.url = url;
        }

        /**
         * @return the title
         */
        public String getTitle() {
            return title;
        }

        /**
         * @param title the title to set
         */
        void setTitle(String title) {
            this.title = title;
        }

        /**
         * @return the visibleUrl
         */
        public String getVisibleUrl() {
            return visibleUrl;
        }

        /**
         * @param visibleUrl the visibleUrl to set
         */
        void setVisibleUrl(String visibleUrl) {
            this.visibleUrl = visibleUrl;
        }

        /**
         * @return the content
         */
        public String getContent() {
            return content;
        }

        /**
         * @param content the content to set
         */
        void setContent(String content) {
            this.content = content;
        }

        /**
         * @return the estimatedResultCount
         */
        public String getEstimatedResultCount() {
            return estimatedResultCount;
        }

        /**
         * @param estimatedResultCount the estimatedResultCount to set
         */
        void setEstimatedResultCount(String estimatedResultCount) {
            this.estimatedResultCount = estimatedResultCount;
        }

        /**
         * @return the currentPageIndex
         */
        public String getCurrentPageIndex() {
            return currentPageIndex;
        }

        /**
         * @param currentPageIndex the currentPageIndex to set
         */
        void setCurrentPageIndex(String currentPageIndex) {
            this.currentPageIndex = currentPageIndex;
        }

        /**
         * @return the tbUrl
         */
        public String getTbUrl() {
            return tbUrl;
        }

        /**
         * @param tbUrl the tbUrl to set
         */
        void setTbUrl(String tbUrl) {
            this.tbUrl = tbUrl;
        }

        /**
         * @return the staticMapUrl
         */
        public String getStaticMapUrl() {
            return staticMapUrl;
        }

        /**
         * @param staticMapUrl the staticMapUrl to set
         */
        void setStaticMapUrl(String staticMapUrl) {
            this.staticMapUrl = staticMapUrl;
        }

        /**
         * @return the tbWidth
         */
        public String getTbWidth() {
            return tbWidth;
        }

        /**
         * @param tbWidth the tbWidth to set
         */
        void setTbWidth(String tbWidth) {
            this.tbWidth = tbWidth;
        }

        /**
         * @return the tbHeight
         */
        public String getTbHeight() {
            return tbHeight;
        }

        /**
         * @param tbHeight the tbHeight to set
         */
        void setTbHeight(String tbHeight) {
            this.tbHeight = tbHeight;
        }
    }
}
