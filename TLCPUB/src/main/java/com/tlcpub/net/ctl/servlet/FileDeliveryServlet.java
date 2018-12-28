package com.tlcpub.net.ctl.servlet;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

import com.tlcpub.net.core.key.Constant;
import com.tlcpub.net.core.util.BeanFinder;
import com.tlcpub.net.core.util.ShellCommander;
import com.tlcpub.net.core.web.servlet.ParameterInjectionServlet;
import com.tlcpub.net.ctl.dto.Server;
import com.tlcpub.net.ctl.service.SchedulerService;


public class FileDeliveryServlet extends ParameterInjectionServlet{

   
   static final long serialVersionUID = 1;
   
   private String fileLocation;
   private int fileMaxSize;
   private String transportUri;
   private String receiveUri;
   private int port;
   
   private int maxMemorySize = 1024 * 1024;
   private File deliveryDir;
   private String hostName;
   
   private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
   
   private SchedulerService schedulerService;
   
   
   @Override
   public void postInit(){
      
      deliveryDir= new File(fileLocation);
      
      String[] names = BeanFinder.getBeanNamesForType(SchedulerService.class);
      if((names == null) || (names.length == 0) || (names[0] == null))
         throw new RuntimeException("Failed to find SchedulerService bean");
      
      schedulerService = (SchedulerService)BeanFinder.getBean(names[0]);
      
      ShellCommander commander = new ShellCommander();
      commander.execute("hostname");
      hostName = commander.getResultString().trim();
   }

   @Override
   public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      
      if(!Constant.HTTP_METHOD_POST.equals(request.getMethod())){
         response.getWriter().println("Invalid http request - "+request.getMethod());
         return;
      }
      
      String requestServletPath = request.getServletPath();
      if(requestServletPath.equals(transportUri)){
         doFileRelay(request, response);
      }else if(requestServletPath.equals(receiveUri)){
         doFileReceive(request, response);
      }else{
         response.sendError(HttpServletResponse.SC_NOT_FOUND);
      }
   }   

   @SuppressWarnings("unchecked")
   private void doFileRelay(HttpServletRequest request, HttpServletResponse response) throws IOException {
      
      response.setContentType("text/plain");
      PrintWriter writer = response.getWriter();
      
      writer.println();
      writer.println("   # File Delivery Report");
      writer.println();
      
      DiskFileItemFactory factory = new DiskFileItemFactory(maxMemorySize, deliveryDir);
      ServletFileUpload fileUpload = new ServletFileUpload(factory);
      fileUpload.setSizeMax(fileMaxSize);
      
      List<FileItem> items = null;
      try{
         items = (List<FileItem>)fileUpload.parseRequest(request);
      }catch(FileUploadException fue){
         logger.error(fue);
         writer.println();
         writer.println("["+hostName+"] Failed to parse multipart request");
         return;
      }
      
      for(FileItem item: items) {
         
         if (item.isFormField()) {
            continue;
         } else if (item.getSize() == 0) {
            continue;
         }
         
         String fileName = item.getName().trim();
         if (fileName.indexOf("\\") > 0) {
            fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
         } else if (fileName.indexOf("/") > 0) {
            fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
         }
         
         writer.println();
         writer.println("# "+ fileName);
         writer.println();
         
         String tempFileName = Long.toString(System.currentTimeMillis());
         File file = new File(deliveryDir, tempFileName);
         try{
            item.write(file);
         }catch(Exception ioe){
            logger.error(ioe);
            writer.println("["+hostName+"] Failed to write temp file");
            continue;
         }

         List<Server> serverList = schedulerService.getServerList();
         for(Server server: serverList){
            try{
               PostMethod postMethod = new PostMethod("http://" + server.getIp() + ":" + port + receiveUri);
                FilePart parts =new FilePart(fileName, file);
                //postMethod.setRequestEntity(new MultipartRequestEntity(parts, postMethod.getParams()));
                
                HttpClient client = new HttpClient() {
					
					@Override
					public HttpParams getParams() {
						// TODO Auto-generated method stub
						return null;
					}
					
					@Override
					public ClientConnectionManager getConnectionManager() {
						// TODO Auto-generated method stub
						return null;
					}
					
					@Override
					public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler,
							HttpContext context) throws IOException, ClientProtocolException {
						// TODO Auto-generated method stub
						return null;
					}
					
					@Override
					public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler)
							throws IOException, ClientProtocolException {
						// TODO Auto-generated method stub
						return null;
					}
					
					@Override
					public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context)
							throws IOException, ClientProtocolException {
						// TODO Auto-generated method stub
						return null;
					}
					
					@Override
					public HttpResponse execute(HttpHost target, HttpRequest request, HttpContext context)
							throws IOException, ClientProtocolException {
						// TODO Auto-generated method stub
						return null;
					}
					
					@Override
					public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler)
							throws IOException, ClientProtocolException {
						// TODO Auto-generated method stub
						return null;
					}
					
					@Override
					public HttpResponse execute(HttpHost target, HttpRequest request) throws IOException, ClientProtocolException {
						// TODO Auto-generated method stub
						return null;
					}
					
					@Override
					public HttpResponse execute(HttpUriRequest request, HttpContext context)
							throws IOException, ClientProtocolException {
						// TODO Auto-generated method stub
						return null;
					}
					
					@Override
					public HttpResponse execute(HttpUriRequest request) throws IOException, ClientProtocolException {
						// TODO Auto-generated method stub
						return null;
					}
				};
                //client.getHttpConnectionManager()getServletConfig().getServletContext().setAttribute("time", 5000);
                
                HttpResponse statusCode = client.execute((HttpUriRequest) request);
                String responseLine = postMethod.getResponseBodyAsString();

               if(HttpStatus.SC_OK!= response.getStatus())
                  writer.println("["+server.getName()+"] HTTP ERROR - "+statusCode);
               else
                  writer.println("["+server.getName()+"] "+responseLine);

            }catch(Exception e){
               writer.println("["+server.getName()+"] "+e.getMessage());
            }
         } // End Server Looping
		 file.delete();
         
         } // End FileItem Looping
   }
   
   @SuppressWarnings("unchecked")
   public void doFileReceive(HttpServletRequest request, HttpServletResponse response) throws IOException{
      
      PrintWriter writer = response.getWriter();
      
      DiskFileItemFactory factory = new DiskFileItemFactory(maxMemorySize, deliveryDir);
      ServletFileUpload fileUpload = new ServletFileUpload(factory);
      fileUpload.setSizeMax(fileMaxSize);
      
      List<FileItem> items = null;
      try {
         items = (List<FileItem>)fileUpload.parseRequest(request);
      } catch(FileUploadException fue){
         logger.error(fue);
         writer.println("Failed to parse multipart request");
         return;
      }

      for(FileItem item: items) {
         if (item.isFormField()) {
            continue;
         } else if (item.getSize() == 0) {
            continue;
         }
         
         String fileName = item.getFieldName();
         if (fileName.indexOf("\\") > 0) {
            fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
         } else if (fileName.indexOf("/") > 0) {
            fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
         }

         String dateStr = dateFormat.format(new Date());
         File saveDir = new File(deliveryDir, dateStr);
         if(!saveDir.exists())
            saveDir.mkdirs();
         
         File file = new File(saveDir, fileName);
         try {
            item.write(file);
            writer.println("SUCCESS");
         } catch(Exception ioe){
            writer.println("Failed to write file");
         }
      }
   }
}
