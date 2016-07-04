/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.web.file;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Maps;
import com.sun.mail.handlers.text_html;
import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion.Static;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.httputil.HostUtil;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.web.Servlets;
import com.thinkgem.jeesite.common.utils.FileUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.school.entity.notice.SchoolNotice;
import com.thinkgem.jeesite.modules.school.entity.tips.SchoolTips;
import com.thinkgem.jeesite.modules.school.service.tips.SchoolTipsService;
import com.thinkgem.jeesite.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thoughtworks.xstream.mapper.Mapper.Null;

/**
 * 文件查询Controller
 * @author 何伟杰
 * @version 2016-05-12
 */
@Controller
@RequestMapping(value = "${adminPath}/school/file/schoolFile")
public class FileController extends BaseController {

	
//	@ModelAttribute
//	public SchoolTips get(@RequestParam(required=false) String id) {
//		SchoolTips entity = null;
//		if (StringUtils.isNotBlank(id)){
//			entity = schoolTipsService.get(id);
//		}
//		if (entity == null){
//			entity = new SchoolTips();
//		}
//		return entity;
//	}
	
	ArrayList<File> AllFiles=new ArrayList<File>();


	/**
	 * g个人文件柜文件查询
	 * 
	 * @return
	 */
	@RequestMapping(value = "getFile", method = RequestMethod.GET)
	@ResponseBody
	public String getFile(@RequestParam(required=true) String path,HttpServletRequest request) {
		Principal principal = (Principal) UserUtils.getPrincipal();
		String baseURL = FileUtils.path("img/" + Global.USERFILES_BASE_URL + principal + "/");
		String baseDir =FileUtils.path(HostUtil.Disk+Global.USERFILES_BASE_URL + principal + "/");

		System.out.println(Global.getUserfilesBaseDir());
		System.out.println(Global.USERFILES_BASE_URL);
		System.out.println(principal);
		System.out.println(request.getLocalAddr());
		System.out.println(request.getLocalPort());
		if(path!=null&&!path.equals("")){
			baseDir=baseDir+path;
		}
		System.out.println(baseDir);
		File file = new File(baseDir);

		File[] folders = file.listFiles();

		ArrayList<String> foldersName = new ArrayList<String>();
		ArrayList<String> filesName = new ArrayList<String>();

		if (folders != null) {
			for (int i = 0; i < folders.length; i++) {
				if(folders[i].getName().equals("_thumbs"))
					continue;
				if (folders[i].isDirectory())
					foldersName.add(folders[i].getName());
				else
					filesName.add(folders[i].getName());
			}
		}
		Map<String, Object> map1 = Maps.newHashMap();
		Map<String, Object> map2 = Maps.newHashMap();

		map2.put("url", HostUtil.host+baseURL);
		map2.put("folder", foldersName);
		map2.put("file", filesName);
		map1.put("message", "success");
		map1.put("data", map2);
		
		
		return JsonMapper.getInstance().toJson(map1);
	}
	
	/**
	 * 文件查询
	 * 
	 * @return
	 */
	@RequestMapping(value = "searchFile", method = RequestMethod.GET)
	@ResponseBody
	public String searchFile(@RequestParam(required=true) String key,HttpServletRequest request) {
		AllFiles.clear();
		Principal principal = (Principal) UserUtils.getPrincipal();
		String baseURL = FileUtils.path("img/" + Global.USERFILES_BASE_URL);
		String baseDir =FileUtils.path(HostUtil.Disk+Global.USERFILES_BASE_URL);

		System.out.println(Global.getUserfilesBaseDir());
		System.out.println(Global.USERFILES_BASE_URL);
		System.out.println(principal);
		System.out.println(request.getLocalAddr());
		System.out.println(request.getLocalPort());

		System.out.println(baseDir);
		File file = new File(baseDir);
		getAllFile(file);
		
		Map<String, Object> map1 = Maps.newHashMap();
		ArrayList<Map<String, Object>> maps = new ArrayList<Map<String,Object>>();
		for(File file2:AllFiles){
			if(file2.getName().equals("_thumbs"))
				continue;
			if(file2.getName().contains(key)){
				Map<String, Object> map2 = Maps.newHashMap();
				String path=FileUtils.path(file2.getAbsolutePath());
				path=path.replace(baseDir, "");
				path=HostUtil.host+baseURL+path;

				String[] temp=path.split("/");

				String id=temp[5];
				map2.put("username",UserUtils.get(id).getName());
				map2.put("date",file2.lastModified());
				map2.put("fileName",file2.getName());
				map2.put("url",path);
				maps.add(map2);
			}
		}

		map1.put("code", "1");
		map1.put("message", "success");
		map1.put("data", maps);
		
		
		return JsonMapper.getInstance().toJson(map1);
	}
	
	/**
	 * 文件分享
	 * 
	 * @return
	 */
	@RequestMapping(value = "shareFile", method = RequestMethod.GET)
	@ResponseBody
	public String shareFile(@RequestParam(required=true) String lessonId,HttpServletRequest request) {
		AllFiles.clear();
		Principal principal = (Principal) UserUtils.getPrincipal();
		String baseURL = FileUtils.path("img/" + Global.USERFILES_BASE_URL);
		String baseDir =FileUtils.path(HostUtil.Disk+Global.USERFILES_BASE_URL);

		System.out.println(Global.getUserfilesBaseDir());
		System.out.println(Global.USERFILES_BASE_URL);
		System.out.println(principal);
		System.out.println(request.getLocalAddr());
		System.out.println(request.getLocalPort());

		System.out.println(baseDir);
		File file = new File(baseDir);
		getAllFolder(file);
		
		Map<String, Object> map1 = Maps.newHashMap();
		ArrayList<Map<String, Object>> maps = new ArrayList<Map<String,Object>>();
		for(File file2:AllFiles){
			if(file2.getName().contains(lessonId)){
                File[] fileArray=file2.listFiles();
                if(fileArray!=null){
                    for (int i = 0; i < fileArray.length; i++) {
        				Map<String, Object> map2 = Maps.newHashMap();
        				String path=FileUtils.path(fileArray[i].getAbsolutePath());
        				path=path.replace(baseDir, "");
        				path=HostUtil.host+baseURL+path;

        				String[] temp=path.split("/");

        				String id=temp[5];
        				map2.put("username",UserUtils.get(id).getName());
        				map2.put("date",fileArray[i].lastModified());
        				map2.put("fileName",fileArray[i].getName());
        				map2.put("url",path);
        				maps.add(map2);
                    }
                }

			}
		}

		map1.put("code", "1");
		map1.put("message", "success");
		map1.put("data", maps);
		
		
		return JsonMapper.getInstance().toJson(map1);
	}
	
	public void getAllFile(File f){

        if(f!=null){
            if(f.isDirectory()){
                File[] fileArray=f.listFiles();
                if(fileArray!=null){
                    for (int i = 0; i < fileArray.length; i++) {
                        //递归调用
                        getAllFile(fileArray[i]);
                    }
                }
            }
            else{
                AllFiles.add(f);
            }
        }
    }
	
	public void getAllFolder(File f){

        if(f!=null){
            if(f.isDirectory()){
                //递归调用
                AllFiles.add(f);
                File[] fileArray=f.listFiles();
                if(fileArray!=null){
                    for (int i = 0; i < fileArray.length; i++) {

                    	getAllFolder(fileArray[i]);
                    }
                }
            }
        }
    }
}