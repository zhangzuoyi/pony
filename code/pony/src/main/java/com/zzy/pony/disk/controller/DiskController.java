package com.zzy.pony.disk.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zzy.pony.dao.UserDao;
import com.zzy.pony.disk.dao.DiskInfoDao;
import com.zzy.pony.disk.dao.DiskShareDao;
import com.zzy.pony.disk.model.DiskInfo;
import com.zzy.pony.disk.model.DiskShare;
import com.zzy.pony.disk.service.TenantHolder;
import com.zzy.pony.disk.store.StoreConnector;
import com.zzy.pony.disk.util.IoUtils;
import com.zzy.pony.disk.util.ServletUtils;
import com.zzy.pony.model.User;

@Controller
@RequestMapping("disk")
public class DiskController {
    private static Logger logger = LoggerFactory
            .getLogger(DiskController.class);
    @Autowired
    private DiskShareDao diskShareDao;
    @Autowired
    private DiskInfoDao diskInfoDao;
    @Autowired
    private StoreConnector storeConnector;
    @Autowired
    private TenantHolder tenantHolder;
    @Autowired
    private UserDao userDao;

    /**
     * 首页.
     */
    @RequestMapping("disk-home")
    public String home(
            @RequestParam(value = "username", required = false) String username,
            Model model) {
        if (username == null) {
            Pageable pageable = new PageRequest(1, 10);
            List<DiskInfo> diskInfos = diskInfoDao.findAll(pageable).getContent();
            List<String> userIds = new ArrayList<String>();
            List<User> userDtos = new ArrayList<User>();

            for (DiskInfo diskInfo : diskInfos) {
                String userId = diskInfo.getCreator();

                if (userIds.contains(userId)) {
                    continue;
                }

                User userDto = userDao.findOne(Integer.valueOf(userId));
                userDtos.add(userDto);
            }

            model.addAttribute("userDtos", userDtos);
        } else {
        	List<User> userDtos = new ArrayList<User>();
        	userDtos.add(userDao.findByLoginName(username));
            model.addAttribute("userDtos", userDtos);
        }

        return "disk/disk-home";
    }

    /**
     * 列表.
     */
    @RequestMapping("disk-list")
    public String list(@RequestParam("u") String u,
            @RequestParam(value = "path", required = false) String path,
            Model model) {
        if (path == null) {
            path = "";
        }

        String userId = u;

        List<DiskShare> diskShares = diskShareDao.findByCreator(userId);
        model.addAttribute("diskShares", diskShares);
        model.addAttribute("path", path);

        return "disk/disk-list";
    }

    /**
     * 详情.
     */
    @RequestMapping("disk-view")
    public String view(
            @RequestParam("id") Long id,
            @CookieValue(value = "share", required = false) String sharePassword,
            Model model) {
        DiskShare diskShare = diskShareDao.findOne(id);

        if ("private".equals(diskShare.getShareType())) {
            if (!diskShare.getSharePassword().equals(sharePassword)) {
                return "disk/disk-code";
            }
        }

        model.addAttribute("diskShare", diskShare);

        return "disk/disk-view";
    }

    /**
     * 下载.
     */
    @RequestMapping("disk-download")
    public void download(@RequestParam("id") Long id,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String tenantId = tenantHolder.getTenantId();
        DiskShare diskShare = diskShareDao.findOne(id);
        DiskInfo diskInfo = diskShare.getDiskInfo();
        InputStream is = null;

        try {
            ServletUtils.setFileDownloadHeader(request, response,
                    diskInfo.getName());

            String modelName = "disk/user/" + diskInfo.getCreator();
            String keyName = diskInfo.getRef();

            is = storeConnector.getStore(modelName, keyName, tenantId)
                    .getDataSource().getInputStream();
            IoUtils.copyStream(is, response.getOutputStream());
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    @RequestMapping("disk-code")
    public String diskCode(@RequestParam("id") Long id,
            @RequestParam("code") String code, HttpServletResponse response) {
        response.addCookie(new Cookie("share", code));

        return "redirect:/disk/disk-view.do?id=" + id;
    }

}
