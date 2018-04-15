package hyman.sr.controller;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

@Controller
@RequestMapping("/file/")
public class FileController {
	@RequestMapping("enterUploadFile.do")
	public String enterUploadFile(){
		return "uploadFile";
	}
	
	
	@RequestMapping("uploadSingleFile.do")
	public void uploadSingleFile(@RequestParam("bfile") MultipartFile bfile,HttpServletRequest request) throws IllegalStateException, IOException{
		System.out.println("filename: "+bfile.getOriginalFilename());
		System.out.println("size: "+bfile.getSize());
		System.out.println("name: "+bfile.getName());
		
		File file=new File(getUploadPath(request)+"/"+bfile.getOriginalFilename()+"_"+getUUID());
		bfile.transferTo(file);
	}
	
	
	public String getUploadPath(HttpServletRequest request){
		String path=request.getServletContext().getRealPath("/filepath");
		return path;
	}
	
	public void saveFile(MultipartFile file,HttpServletRequest request) throws IllegalStateException, IOException{
		String path=getUploadPath(request);
		File file2=new File(path+"/"+file.getOriginalFilename()+"_"+getUUID());
		file.transferTo(file2);
	}
	
	public String getUUID(){
		return UUID.randomUUID().toString();
	}
	
	
	@RequestMapping("multiFileUpload.do")
	public void multiFileUpload(MultipartRequest files,HttpServletRequest request) throws IllegalStateException, IOException{
		Map<String,MultipartFile> keyMap = files.getFileMap();
		Set<String> keys = keyMap.keySet();
		for(String key : keys){
			MultipartFile file = keyMap.get(key);
			saveFile(file,request);
		}
	}
}	
