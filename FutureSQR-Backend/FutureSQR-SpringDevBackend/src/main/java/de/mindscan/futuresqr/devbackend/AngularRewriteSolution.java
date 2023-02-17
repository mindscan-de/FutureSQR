/**
 * 
 * MIT License
 *
 * Copyright (c) 2023 Maxim Gansert, Mindscan
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 */
package de.mindscan.futuresqr.devbackend;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.valves.rewrite.RewriteValve;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;

// uncomment this to enable this solution
//@Component
public class AngularRewriteSolution implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

	@Override
	public void customize(TomcatServletWebServerFactory factory) {
		final RewriteValve valve = new RewriteValve() {

			@Override
			protected synchronized void startInternal() throws LifecycleException {
				super.startInternal();

//				parse("RewriteCond %{REQUEST_URI} !^.*\\.(bmp|css|gif|htc|html?|ico|jpe?g|js|pdf|png|swf|txt|xml|svg|eot|woff|woff2|ttf|map)$");
//				parse("RewriteRule ^(.*)$ /index.html [L]");
				parse("RewriteCond %{SERVLET_PATH} !-f");
//				parse("RewriteRule ^/frontend/(.*)$ /frontend/index.html [L]");
				parse("RewriteRule ^/(.*)$ /index.html [L]");

			}
		};

		valve.setEnabled(true);

		factory.addContextValves(valve);
	}

}
