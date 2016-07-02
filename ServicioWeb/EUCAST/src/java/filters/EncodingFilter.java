package filters;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.SortedMap;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

  public class EncodingFilter implements Filter
   {
   private FilterConfig filterConfig;
    private String charsetDefault;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        this.charsetDefault = "UTF-8";
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String charset = this.filterConfig.getInitParameter("charset");
        SortedMap m = Charset.availableCharsets();
        if (m.containsKey(charset)) {
            request.setCharacterEncoding(charset);
            response.setCharacterEncoding(charset);
        } else {
            request.setCharacterEncoding(this.charsetDefault);
            response.setCharacterEncoding(this.charsetDefault);
        }
        chain.doFilter(request, response);
    }

    public void destroy() {
    }
  }