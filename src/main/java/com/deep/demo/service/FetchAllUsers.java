package com.deep.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.naming.*;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

@Service
@Slf4j
@RequiredArgsConstructor
public class FetchAllUsers {

    @Value("${username}")
    private String user ;

    @Value("${pwdUser}")
    private String ldapPW ;

    public String getAttributes(String user) {
        Attributes attributes=null;
        String url = "ldap://homeoffice.wal-mart.com:3268";
        String domainName = "homeoffice.wal-mart.com";
        String principalName = user+ "@" + domainName;
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, url);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, principalName);
        env.put(Context.SECURITY_CREDENTIALS, ldapPW);
        String searchbase = "DC=homeoffice,DC=Wal-Mart,DC=com";
        String searchfilter = "sAMAccountName=" + user;
        NamingEnumeration results = null;

        try {
            DirContext ctx = new InitialDirContext(env);
            SearchControls controls = new SearchControls();
            controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            results = ctx.search(searchbase, searchfilter, controls);
            if (results.hasMore()) {
                SearchResult searchResult = (SearchResult) results.next();
                attributes = searchResult.getAttributes();
            }
        } catch (AuthenticationNotSupportedException ex) {
            log.error("The authentication is not supported by the server", ex);
        } catch (AuthenticationException ex) {
            log.error("incorrect password or username", ex);
        } catch (NamingException ex) {
            log.error("error when trying to create the context", ex);
        }
        return attributes.toString();
    }

    public String getAttributesForGroup(String adGroup) {
        ArrayList<Attributes> attributes= new ArrayList<>();
        String url = "ldap://homeoffice.wal-mart.com:3268";
        String domainName = "homeoffice.wal-mart.com";
        String principalName = user+ "@" + domainName;
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, url);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, principalName);
        env.put(Context.SECURITY_CREDENTIALS, ldapPW);
        String searchbase = "DC=Wal-Mart,DC=com";
        String filterSearch = "";
        String srsFilter = sanitizeInputForXss("(memberOf=CN=" + adGroup + ",OU=SRS,OU=AppManaged,OU=Groups,OU=IDM,DC=homeoffice,DC=" +
                "Wal-Mart,DC=com)");
        String sailpointFilter = sanitizeInputForXss("(memberOf=CN=" + adGroup + ",OU=SailPoint,OU=AppManaged,OU=Groups,OU=IDM,DC=homeoffice,DC=" +
                "Wal-Mart,DC=com)");
        filterSearch = filterSearch.concat(srsFilter);
        filterSearch = filterSearch.concat(sailpointFilter);

        int totalResults = 0;
        List<String> usersList = new ArrayList<String>();
        NamingEnumeration results = null;

        try {
            DirContext ctx = new InitialDirContext(env);
            SearchControls controls = new SearchControls();
            controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            results = ctx.search(searchbase,sanitizeInputForXss("(|" + filterSearch + ")"), controls);

            while (results.hasMoreElements()) {
                SearchResult sr = (SearchResult) results.next();
                totalResults++;

                String names[] = sr.getName().split(",");
                String name[] = names[0].split("=");
                usersList.add(name[1]);

            }
            System.out.println("Total number of users in AD server : " + totalResults);
            System.out.println(usersList);
            return usersList.toString();

        } catch (AuthenticationNotSupportedException ex) {
            log.error("The authentication is not supported by the server", ex);
            return "Not ok";
        } catch (AuthenticationException ex) {
            log.error("incorrect password or username", ex);
            return "Not ok";
        } catch (NamingException ex) {
            log.error("error when trying to create the context", ex);
            return "Not ok";
        }
    }
    public String fetchUserList() {
        String adGroup = "GL-MJEMS-Admin-non-prod";
        ArrayList<Attributes> attributes= new ArrayList<>();
        String url = "ldap://homeoffice.wal-mart.com:3268";
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, url);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, user);
        env.put(Context.SECURITY_CREDENTIALS, ldapPW);
        String searchbase = "DC=Wal-Mart,DC=com";
        String filterSearch = "";
        String srsFilter = sanitizeInputForXss("(memberOf=CN=" + adGroup + ",OU=SRS,OU=AppManaged,OU=Groups,OU=IDM,DC=homeoffice,DC=" +
                "Wal-Mart,DC=com)");
        String sailpointFilter = sanitizeInputForXss("(memberOf=CN=" + adGroup + ",OU=SailPoint,OU=AppManaged,OU=Groups,OU=IDM,DC=homeoffice,DC=" +
                "Wal-Mart,DC=com)");
        filterSearch = filterSearch.concat(srsFilter);
        filterSearch = filterSearch.concat(sailpointFilter);

        try {

            List<String> usersList = new ArrayList<String>();
            LdapContext ctx = new InitialLdapContext(env, null);
            SearchControls searchCtls = new SearchControls();
            searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            int totalResults = 0;
            NamingEnumeration<SearchResult> fetchData = ctx.search(searchbase, filterSearch, searchCtls);
            while (fetchData.hasMoreElements()) {
                SearchResult sr = (SearchResult) fetchData.next();
                totalResults++;

                String names[] = sr.getName().split(",");
                String name[] = names[0].split("=");
                usersList.add(name[1]);

            }
            System.out.println("Total number of users in AD server : " + totalResults);
            System.out.println(usersList);
            return usersList.toString();
        } catch (NamingException e) {
            e.printStackTrace();
            return "not ok ";
        } catch (Exception e) {
            e.printStackTrace();
            return "not ok ";
        }
    }

    public static ArrayList<String> getMemberships(Attributes attributes) throws NamingException {
        ArrayList<String> response = new ArrayList<String>();
        Attribute attr = attributes.get("memberOf");
        for (int i = 0; i < attr.size(); i++) {
            String att = attr.get(i).toString();
            if (att.toUpperCase().startsWith("CN="))
                response.add(getCN(att));
        }
        return response;
    }

    public static String getNameForADGroup(String  name) {
        int position = name.indexOf('-');
        if( position == -1){
            return name.trim();
        } else{
            return name.substring(0,position).trim();
        }

    }

    public static String getName(Attributes attributes) throws NamingException {
        return getNameForADGroup(attributes.get("name").get(0).toString());
    }

    public static String getEmail(Attributes attributes) throws NamingException {

        if( attributes.get("mail") != null){
            return attributes.get("mail").get(0).toString();
        }
        else{
            return  null;
        }
    }


    public static String getCN(String cnName) {
        if (cnName != null && cnName.toUpperCase().startsWith("CN=")) {
            cnName = cnName.substring(3);
        }
        int position = cnName.indexOf(',');
        if (position == -1) {
            return cnName;
        } else {
            return cnName.substring(0, position);
        }
    }

    public static String sanitizeInputForXss(String str) {
        String sanitized = null;
        if (str != null) {
            sanitized = Jsoup.clean(StringEscapeUtils.escapeHtml4(str), Safelist.basic());
        }
        return sanitized;
    }
}


