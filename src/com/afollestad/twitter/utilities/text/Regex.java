package com.afollestad.twitter.utilities.text;

/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Regex {
    /**
     * Regular expression pattern to match all IANA top-level domains.
     * List accurate as of 2007/06/15.  List taken from:
     * http://data.iana.org/TLD/tlds-alpha-by-domain.txt
     * This pattern is auto-generated by //device/tools/make-iana-tld-pattern.py
     */
    public static final Pattern TOP_LEVEL_DOMAIN_PATTERN
            = Pattern.compile(
            "((aero|arpa|asia|a[cdefgilmnoqrstuwxz])"
                    + "|(biz|b[abdefghijmnorstvwyz])"
                    + "|(cat|com|coop|c[acdfghiklmnoruvxyz])"
                    + "|d[ejkmoz]"
                    + "|(edu|e[cegrstu])"
                    + "|f[ijkmor]"
                    + "|(gov|g[abdefghilmnpqrstuwy])"
                    + "|h[kmnrtu]"
                    + "|(info|int|i[delmnoqrst])"
                    + "|(jobs|j[emop])"
                    + "|k[eghimnrwyz]"
                    + "|l[abcikrstuvy]"
                    + "|(mil|mobi|museum|m[acdghklmnopqrstuvwxyz])"
                    + "|(name|net|n[acefgilopruz])"
                    + "|(org|om)"
                    + "|(pro|p[aefghklmnrstwy])"
                    + "|qa"
                    + "|r[eouw]"
                    + "|s[abcdeghijklmnortuvyz]"
                    + "|(tel|travel|t[cdfghjklmnoprtvwz])"
                    + "|u[agkmsyz]"
                    + "|v[aceginu]"
                    + "|w[fs]"
                    + "|y[etu]"
                    + "|z[amw])");

    /**
     * Regular expression pattern to match RFC 1738 URLs
     * List accurate as of 2007/06/15.  List taken from:
     * http://data.iana.org/TLD/tlds-alpha-by-domain.txt
     * This pattern is auto-generated by //device/tools/make-iana-tld-pattern.py
     */
    public static final Pattern WEB_URL_PATTERN
            = Pattern.compile(
            "((?:(http|https|Http|Https):\\/\\/(?:(?:[a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)"
                    + "\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,64}(?:\\:(?:[a-zA-Z0-9\\$\\-\\_"
                    + "\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,25})?\\@)?)?"
                    + "((?:(?:[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}\\.)+"   // named host
                    + "(?:"   // plus top level domain
                    + "(?:aero|arpa|asia|a[cdefgilmnoqrstuwxz])"
                    + "|(?:biz|b[abdefghijmnorstvwyz])"
                    + "|(?:cat|com|coop|c[acdfghiklmnoruvxyz])"
                    + "|d[ejkmoz]"
                    + "|(?:edu|e[cegrstu])"
                    + "|f[ijkmor]"
                    + "|(?:gov|g[abdefghilmnpqrstuwy])"
                    + "|h[kmnrtu]"
                    + "|(?:info|int|i[delmnoqrst])"
                    + "|(?:jobs|j[emop])"
                    + "|k[eghimnrwyz]"
                    + "|l[abcikrstuvy]"
                    + "|(?:mil|mobi|museum|m[acdghklmnopqrstuvwxyz])"
                    + "|(?:name|net|n[acefgilopruz])"
                    + "|(?:org|om)"
                    + "|(?:pro|p[aefghklmnrstwy])"
                    + "|qa"
                    + "|r[eouw]"
                    + "|s[abcdeghijklmnortuvyz]"
                    + "|(?:tel|travel|t[cdfghjklmnoprtvwz])"
                    + "|u[agkmsyz]"
                    + "|v[aceginu]"
                    + "|w[fs]"
                    + "|y[etu]"
                    + "|z[amw]))"
                    + "|(?:(?:25[0-5]|2[0-4]" // or ip address
                    + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(?:25[0-5]|2[0-4][0-9]"
                    + "|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1]"
                    + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
                    + "|[1-9][0-9]|[0-9])))"
                    + "(?:\\:\\d{1,5})?)" // plus option port number
                    + "(\\/(?:(?:[a-zA-Z0-9\\;\\/\\?\\:\\@\\&\\=\\#\\~"  // plus option query params
                    + "\\-\\.\\+\\!\\*\\'\\(\\)\\,\\_])|(?:\\%[a-fA-F0-9]{2}))*)?"
                    + "(?:\\b|$)"); // and finally, a word boundary or end of
    // input.  This is to stop foo.sure from
    // matching as foo.su

    public static final Pattern IP_ADDRESS_PATTERN
            = Pattern.compile(
            "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
                    + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
                    + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
                    + "|[1-9][0-9]|[0-9]))");

    public static Pattern DOMAIN_NAME_PATTERN
            = Pattern.compile(
            "(((([a-zA-Z0-9][a-zA-Z0-9\\-]*)*[a-zA-Z0-9]\\.)+"
                    + TOP_LEVEL_DOMAIN_PATTERN + ")|"
                    + IP_ADDRESS_PATTERN + ")");

    public static final Pattern EMAIL_ADDRESS_PATTERN
            = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    /**
     * This pattern is intended for searching for things that look like they
     * might be phone numbers in arbitrary text, not for validating whether
     * something is in fact a phone number.  It will miss many things that
     * are legitimate phone numbers.
     * <p/>
     * <p> The pattern matches the following:
     * <ul>
     * <li>Optionally, a + sign followed immediately by one or more digits. Spaces, dots, or dashes
     * may follow.
     * <li>Optionally, sets of digits in parentheses, separated by spaces, dots, or dashes.
     * <li>A string starting and ending with a digit, containing digits, spaces, dots, and/or dashes.
     * </ul>
     */
    public static final Pattern PHONE_PATTERN
            = Pattern.compile(                                  // sdd = space, dot, or dash
            "(\\+[0-9]+[\\- \\.]*)?"                    // +<digits><sdd>*
                    + "(\\([0-9]+\\)[\\- \\.]*)?"               // (<digits>)<sdd>*
                    + "([0-9][0-9\\- \\.][0-9\\- \\.]+[0-9])"); // <digit><digit|sdd>+<digit>

    /**
     * Convenience method to take all of the non-null matching groups in a
     * regex Matcher and return them as a concatenated string.
     *
     * @param matcher The Matcher object from which grouped text will
     *                be extracted
     * @return A String comprising all of the non-null matched
     *         groups concatenated together
     */
    public static String concatGroups(Matcher matcher) {
        StringBuilder b = new StringBuilder();
        final int numGroups = matcher.groupCount();

        for (int i = 1; i <= numGroups; i++) {
            String s = matcher.group(i);

            System.err.println("Group(" + i + ") : " + s);

            if (s != null) {
                b.append(s);
            }
        }

        return b.toString();
    }

    /**
     * Convenience method to return only the digits and plus signs
     * in the matching string.
     *
     * @param matcher The Matcher object from which digits and plus will
     *                be extracted
     * @return A String comprising all of the digits and plus in
     *         the match
     */
    public static String digitsAndPlusOnly(Matcher matcher) {
        StringBuilder buffer = new StringBuilder();
        String matchingRegion = matcher.group();

        for (int i = 0, size = matchingRegion.length(); i < size; i++) {
            char character = matchingRegion.charAt(i);

            if (character == '+' || Character.isDigit(character)) {
                buffer.append(character);
            }
        }
        return buffer.toString();
    }
}
