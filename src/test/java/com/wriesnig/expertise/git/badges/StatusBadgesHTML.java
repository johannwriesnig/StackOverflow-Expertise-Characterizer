package com.wriesnig.expertise.git.badges;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class StatusBadgesHTML {
    private static final String HTML_BUILD_FAILING = """
            <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" width="80" height="20" role="img"
                 aria-label="build: failing"><title>build: failing</title>
                <linearGradient id="s" x2="0" y2="100%">
                    <stop offset="0" stop-color="#bbb" stop-opacity=".1"/>
                    <stop offset="1" stop-opacity=".1"/>
                </linearGradient>
                <clipPath id="r">
                    <rect width="80" height="20" rx="3" fill="#fff"/>
                </clipPath>
                <g clip-path="url(#r)">
                    <rect width="37" height="20" fill="#555"/>
                    <rect x="37" width="43" height="20" fill="#e05d44"/>
                    <rect width="80" height="20" fill="url(#s)"/>
                </g>
                <g fill="#fff" text-anchor="middle" font-family="Verdana,Geneva,DejaVu Sans,sans-serif"
                   text-rendering="geometricPrecision" font-size="110">
                    <text aria-hidden="true" x="195" y="150" fill="#010101" fill-opacity=".3" transform="scale(.1)"
                          textLength="270">build
                    </text>
                    <text x="195" y="140" transform="scale(.1)" fill="#fff" textLength="270">build</text>
                    <text aria-hidden="true" x="575" y="150" fill="#010101" fill-opacity=".3" transform="scale(.1)"
                          textLength="330">failing
                    </text>
                    <text x="575" y="140" transform="scale(.1)" fill="#fff" textLength="330">failing</text>
                </g>
            </svg>
            """;

    private static final String HTML_BUILD_PASSING = """
            <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" width="88" height="20" role="img"
                 aria-label="build: passing"><title>build: passing</title>
                 <linearGradient id="s" x2="0" y2="100%">
                     <stop offset="0" stop-color="#bbb" stop-opacity=".1"/>
                     <stop offset="1" stop-opacity=".1"/>
                 </linearGradient>
                 <clipPath id="r">
                      <rect width="88" height="20" rx="3" fill="#fff"/>
                 </clipPath>
                 <g clip-path="url(#r)">
                      <rect width="37" height="20" fill="#555"/>
                      <rect x="37" width="51" height="20" fill="#4c1"/>
                      <rect width="88" height="20" fill="url(#s)"/>
                 </g>
                 <g fill="#fff" text-anchor="middle" font-family="Verdana,Geneva,DejaVu Sans,sans-serif"
                      text-rendering="geometricPrecision" font-size="110">
                      <text aria-hidden="true" x="195" y="150" fill="#010101" fill-opacity=".3" transform="scale(.1)"
                          textLength="270">build
                      </text>
                      <text x="195" y="140" transform="scale(.1)" fill="#fff" textLength="270">build</text>
                      <text aria-hidden="true" x="615" y="150" fill="#010101" fill-opacity=".3" transform="scale(.1)"
                           textLength="410">passing
                      </text>
                      <text x="615" y="140" transform="scale(.1)" fill="#fff" textLength="410">passing</text>
                 </g>
            </svg>
            """;

    private static final String HTML_CODE_COV_76 = """
            <svg xmlns="http://www.w3.org/2000/svg" width="112" height="20">
                             <linearGradient id="b" x2="0" y2="100%">
                                 <stop offset="0" stop-color="#bbb" stop-opacity=".1" />
                                 <stop offset="1" stop-opacity=".1" />
                             </linearGradient>
                             <mask id="a">
                                 <rect width="112" height="20" rx="3" fill="#fff" />
                             </mask>
                             <g mask="url(#a)">
                                 <path fill="#555" d="M0 0h76v20H0z" />
                                 <path fill="#fe7d37" d="M76 0h36v20H76z" />
                                 <path fill="url(#b)" d="M0 0h112v20H0z" />
                             </g>
                             <g fill="#fff" text-anchor="middle" font-family="DejaVu Sans,Verdana,Geneva,sans-serif" font-size="11">
                                 <text x="46" y="15" fill="#010101" fill-opacity=".3">codecov</text>
                                 <text x="46" y="14">codecov</text>
                                 <text x="93" y="15" fill="#010101" fill-opacity=".3">76%</text>
                                 <text x="93" y="14">76%</text>
                             </g>
                             <svg viewBox="120 -8 60 60">
                                 <path d="M23.013 0C10.333.009.01 10.22 0 22.762v.058l3.914 2.275.053-.036a11.291 11.291 0 0 1 8.352-1.767 10.911 10.911 0 0 1 5.5 2.726l.673.624.38-.828c.368-.802.793-1.556 1.264-2.24.19-.276.398-.554.637-.851l.393-.49-.484-.404a16.08 16.08 0 0 0-7.453-3.466 16.482 16.482 0 0 0-7.705.449C7.386 10.683 14.56 5.016 23.03 5.01c4.779 0 9.272 1.84 12.651 5.18 2.41 2.382 4.069 5.35 4.807 8.591a16.53 16.53 0 0 0-4.792-.723l-.292-.002a16.707 16.707 0 0 0-1.902.14l-.08.012c-.28.037-.524.074-.748.115-.11.019-.218.041-.327.063-.257.052-.51.108-.75.169l-.265.067a16.39 16.39 0 0 0-.926.276l-.056.018c-.682.23-1.36.511-2.016.838l-.052.026c-.29.145-.584.305-.899.49l-.069.04a15.596 15.596 0 0 0-4.061 3.466l-.145.175c-.29.36-.521.666-.723.96-.17.247-.34.513-.552.864l-.116.199c-.17.292-.32.57-.449.824l-.03.057a16.116 16.116 0 0 0-.843 2.029l-.034.102a15.65 15.65 0 0 0-.786 5.174l.003.214a21.523 21.523 0 0 0 .04.754c.009.119.02.237.032.355.014.145.032.29.049.432l.01.08c.01.067.017.133.026.197.034.242.074.48.119.72.463 2.419 1.62 4.836 3.345 6.99l.078.098.08-.095c.688-.81 2.395-3.38 2.539-4.922l.003-.029-.014-.025a10.727 10.727 0 0 1-1.226-4.956c0-5.76 4.545-10.544 10.343-10.89l.381-.014a11.403 11.403 0 0 1 6.651 1.957l.054.036 3.862-2.237.05-.03v-.056c.006-6.08-2.384-11.793-6.729-16.089C34.932 2.361 29.16 0 23.013 0" fill="#F01F7A" fill-rule="evenodd"/>
                             </svg>
                         </svg>
            """;

    private static final String HTML_CODE_COV_NO_VALUE = """
            <svg xmlns="http://www.w3.org/2000/svg" width="112" height="20">
                             <linearGradient id="b" x2="0" y2="100%">
                                 <stop offset="0" stop-color="#bbb" stop-opacity=".1" />
                                 <stop offset="1" stop-opacity=".1" />
                             </linearGradient>
                             <mask id="a">
                                 <rect width="112" height="20" rx="3" fill="#fff" />
                             </mask>
                             <g mask="url(#a)">
                                 <path fill="#555" d="M0 0h76v20H0z" />
                                 <path fill="#fe7d37" d="M76 0h36v20H76z" />
                                 <path fill="url(#b)" d="M0 0h112v20H0z" />
                             </g>
                             <g fill="#fff" text-anchor="middle" font-family="DejaVu Sans,Verdana,Geneva,sans-serif" font-size="11">
                                 <text x="46" y="15" fill="#010101" fill-opacity=".3">codecov</text>
                                 <text x="46" y="14">codecov</text>
                                 <text x="93" y="15" fill="#010101" fill-opacity=".3">76%</text>
                                 <text x="93" y="14">%</text>
                             </g>
                             <svg viewBox="120 -8 60 60">
                                 <path d="M23.013 0C10.333.009.01 10.22 0 22.762v.058l3.914 2.275.053-.036a11.291 11.291 0 0 1 8.352-1.767 10.911 10.911 0 0 1 5.5 2.726l.673.624.38-.828c.368-.802.793-1.556 1.264-2.24.19-.276.398-.554.637-.851l.393-.49-.484-.404a16.08 16.08 0 0 0-7.453-3.466 16.482 16.482 0 0 0-7.705.449C7.386 10.683 14.56 5.016 23.03 5.01c4.779 0 9.272 1.84 12.651 5.18 2.41 2.382 4.069 5.35 4.807 8.591a16.53 16.53 0 0 0-4.792-.723l-.292-.002a16.707 16.707 0 0 0-1.902.14l-.08.012c-.28.037-.524.074-.748.115-.11.019-.218.041-.327.063-.257.052-.51.108-.75.169l-.265.067a16.39 16.39 0 0 0-.926.276l-.056.018c-.682.23-1.36.511-2.016.838l-.052.026c-.29.145-.584.305-.899.49l-.069.04a15.596 15.596 0 0 0-4.061 3.466l-.145.175c-.29.36-.521.666-.723.96-.17.247-.34.513-.552.864l-.116.199c-.17.292-.32.57-.449.824l-.03.057a16.116 16.116 0 0 0-.843 2.029l-.034.102a15.65 15.65 0 0 0-.786 5.174l.003.214a21.523 21.523 0 0 0 .04.754c.009.119.02.237.032.355.014.145.032.29.049.432l.01.08c.01.067.017.133.026.197.034.242.074.48.119.72.463 2.419 1.62 4.836 3.345 6.99l.078.098.08-.095c.688-.81 2.395-3.38 2.539-4.922l.003-.029-.014-.025a10.727 10.727 0 0 1-1.226-4.956c0-5.76 4.545-10.544 10.343-10.89l.381-.014a11.403 11.403 0 0 1 6.651 1.957l.054.036 3.862-2.237.05-.03v-.056c.006-6.08-2.384-11.793-6.729-16.089C34.932 2.361 29.16 0 23.013 0" fill="#F01F7A" fill-rule="evenodd"/>
                             </svg>
                         </svg>
            """;

    private static final String HTML_CODE_COVERAGE_86 = """
            <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" width="96" height="20" role="img"
                 aria-label="coverage: 86%"><title>coverage: 86%</title>
                <linearGradient id="s" x2="0" y2="100%">
                    <stop offset="0" stop-color="#bbb" stop-opacity=".1"/>
                    <stop offset="1" stop-opacity=".1"/>
                </linearGradient>
                <clipPath id="r">
                    <rect width="96" height="20" rx="3" fill="#fff"/>
                </clipPath>
                <g clip-path="url(#r)">
                    <rect width="61" height="20" fill="#555"/>
                    <rect x="61" width="35" height="20" fill="#4c1"/>
                    <rect width="96" height="20" fill="url(#s)"/>
                </g>
                <g fill="#fff" text-anchor="middle" font-family="Verdana,Geneva,DejaVu Sans,sans-serif"
                   text-rendering="geometricPrecision" font-size="110">
                    <text aria-hidden="true" x="315" y="150" fill="#010101" fill-opacity=".3" transform="scale(.1)"
                          textLength="510">coverage
                    </text>
                    <text x="315" y="140" transform="scale(.1)" fill="#fff" textLength="510">coverage</text>
                    <text aria-hidden="true" x="775" y="150" fill="#010101" fill-opacity=".3" transform="scale(.1)"
                          textLength="250">86%
                    </text>
                    <text x="775" y="140" transform="scale(.1)" fill="#fff" textLength="250">86%</text>
                </g>
            </svg>
            """;

    public static final String LINK_BUILD_PASSING = "https://website.com/buildPassing";
    public static final String LINK_BUILD_FAILING = "https://website.com/buildFailing";
    public static final String LINK_CODE_COV_86 = "https://website.com/codecoverage86";
    public static final String LINK_CODE_COV_76 = "https://website.com/codecove76";
    public static final String LINK_CODE_COV_NO_VALUE = "https://website.com/codecove76";

    public static final String READ_ME_ME_BUILD_FAILING_AND_CODE_COV_76 = """
            Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore
            magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum.
            [![Build Status]("""+LINK_BUILD_FAILING+"""
            )](https://randomsite.com)
            [![Code Coverage]("""+ LINK_CODE_COV_76 +"""
            )](https://randomsite.com)
             
            Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore
            magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum.            
            """;

    public static final String READ_ME_BUILD_PASSING_AND_CODE_COV_86_READ = """
            Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore
            magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum.
            [![Build Status](""" +LINK_BUILD_PASSING + """
            )](https://randomsite.com)
            [![Code Coverage]("""+ LINK_CODE_COV_86 +"""
            )](https://randomsite.com)
          
            Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore
            magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum.              
            """;

    public static final String READ_ME_NO_BUILD_AND_COVE_COV_NO_VALUE = """
            Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore
            magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum.
            [![Code Coverage]("""+LINK_CODE_COV_NO_VALUE+"""
            )](https://randomsite.com)
            """;

    public static InputStream getBuildFailing(){
        return new ByteArrayInputStream(HTML_BUILD_FAILING.getBytes(StandardCharsets.UTF_8));
    }
    public static InputStream getBuildPassing(){
        return new ByteArrayInputStream(HTML_BUILD_PASSING.getBytes(StandardCharsets.UTF_8));
    }
    public static InputStream getCodeCov76(){
        return new ByteArrayInputStream(HTML_CODE_COV_76.getBytes(StandardCharsets.UTF_8));
    }
    public static InputStream getCodeCoverage86(){
        return new ByteArrayInputStream(HTML_CODE_COVERAGE_86.getBytes(StandardCharsets.UTF_8));
    }

    public static InputStream getCodeCovNoValue(){
        return new ByteArrayInputStream(HTML_CODE_COV_NO_VALUE.getBytes(StandardCharsets.UTF_8));
    }

}
