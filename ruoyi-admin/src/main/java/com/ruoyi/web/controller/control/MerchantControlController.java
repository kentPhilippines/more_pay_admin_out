package com.ruoyi.web.controller.control;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.ruoyi.alipay.domain.AlipayDealOrderApp;
import com.ruoyi.alipay.domain.AlipayUserInfo;
import com.ruoyi.alipay.service.IAlipayUserInfoService;
import com.ruoyi.alipay.service.IMerchantInfoEntityService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.StaticConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.RSAUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * 商户风控Controller
 *
 * @author ruoyi
 * @date 2020-03-23
 */
@Controller
@RequestMapping("/control/account")
public class MerchantControlController extends BaseController {

    private String prefix = "control/account";

    @Autowired
    private IMerchantInfoEntityService merchantInfoEntityService;

    @Autowired
    private IAlipayUserInfoService alipayUserInfoService;

    @RequiresPermissions("control:account:merchant:view")
    @GetMapping()
    public String merchant() {
        return prefix + "/merchant";
    }

    /**
     * 查询商户风控信息列表
     */
    @RequiresPermissions("control:account:merchant:list")
    @PostMapping("/merchant/list")
    @ResponseBody
    public TableDataInfo list(AlipayUserInfo merchantInfoEntity) {
        startPage();
        List<AlipayUserInfo> list = merchantInfoEntityService.selectMerchantControlList(merchantInfoEntity);
        return getDataTable(list);
    }

    /**
     * 显示商户风控信息
     */
    @GetMapping("/merchant/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        AlipayUserInfo userInfo = merchantInfoEntityService.selectMerchantInfoEntityById(id);
        mmap.put("alipayUserInfo", userInfo);
        return prefix + "/merchant_edit";
    }

    //如果请求alipay接口走下面的方法
//    /**
//     * 修改保存商户信息
//     */
//    @RequiresPermissions("alipay:merchant:edit")
//    @Log(title = "商户信息", businessType = BusinessType.UPDATE)
//    @PostMapping("/edit")
//    @ResponseBody
//    public AjaxResult editSave(AlipayUserInfo merchantInfoEntity) {
//        //获取alipay处理接口URL
//        String ipPort = dictionaryUtils.getApiUrlPath(StaticConstants.ALIPAY_IP_URL_KEY, StaticConstants.ALIPAY_IP_URL_VALUE);
//        String urlPath = dictionaryUtils.getApiUrlPath(StaticConstants.ALIPAY_ERVICE_API_KEY, StaticConstants.ALIPAY_ERVICE_API_VALUE);
//        //获取数据库内请求路径
//        Map<String, Object> mapParam = Collections.synchronizedMap(Maps.newHashMap());
//        mapParam.put("id", merchantInfoEntity.getId());
//        mapParam.put("userId", merchantInfoEntity.getUserId());
//        mapParam.put("userName", merchantInfoEntity.getUserName());
//        mapParam.put("email", merchantInfoEntity.getEmail());
//        mapParam.put("QQ", merchantInfoEntity.getQQ());
//        mapParam.put("telegram", merchantInfoEntity.getTelegram());
//        mapParam.put("userNode", merchantInfoEntity.getUserNode());
//        AlipayUserInfo alipayUserInfo = merchantInfoEntityService.selectMerchantInfoEntityById(merchantInfoEntity.getId());
//        //对参数进行RSA加密处理
//        String cipherText = RSAUtils.getEncryptPublicKey(mapParam, alipayUserInfo.getPublicKey());
//        String pathParam = "userId=" + merchantInfoEntity.getUserId() + "&cipherText=" + cipherText;
//        //post调用alipay方法
//        String flag = HttpUtils.sendPost(ipPort + urlPath, pathParam);
//        if ("ConnectException".equals(flag)) {
//            throw new BusinessException("操作失败，请求alipay接口地址超时,URL=" + ipPort + urlPath);
//        }
//        JSONObject json = JSONObject.parseObject(flag);
//        String result = json.getString("success");
//        switch (result) {
//            case "true":
//                return toAjax(1);
//            case "false":
//                String message = json.getString("message");
//                return error(message);
//        }
//        return null;
//    }

    /**
     * 修改保存商户风控信息
     */
    @RequiresPermissions("control:account:merchant:edit")
    @Log(title = "商户风控", businessType = BusinessType.UPDATE)
    @PostMapping("/merchant/save")
    @ResponseBody
    public AjaxResult editSave(AlipayUserInfo alipayUserInfo) {
        return toAjax(merchantInfoEntityService.updateMerchantInfoEntity(alipayUserInfo));
    }

    /**
     * 显示码商列表
     *
     * @return
     */
    @RequiresPermissions("control:account:qrOwner:view")
    @GetMapping("/qr/show")
    public String qr() {
        return prefix + "/qr";
    }

    /**
     * 查询码商信息列表
     */
    @RequiresPermissions("control:account:qrOwner:list")
    @PostMapping("/qr/list")
    @ResponseBody
    public TableDataInfo qrList(AlipayUserInfo alipayUserInfo) {
        startPage();
        alipayUserInfo.setUserType(2);
        List<AlipayUserInfo> list = alipayUserInfoService.selectAlipayUserInfoByControl(alipayUserInfo);
        return getDataTable(list);
    }

    /**
     * 显示码商风控信息
     */
    @GetMapping("/qr/edit/{id}")
    public String qrEdit(@PathVariable("id") Long id, ModelMap mmap) {
        AlipayUserInfo userInfo = merchantInfoEntityService.selectMerchantInfoEntityById(id);
        mmap.put("alipayUserInfo", userInfo);
        return prefix + "/qr_edit";
    }

    /**
     * 修改保存商户风控信息
     */
    @RequiresPermissions("control:account:qrOwner:edit")
    @Log(title = "商户风控", businessType = BusinessType.UPDATE)
    @PostMapping("/qr/save")
    @ResponseBody
    public AjaxResult qrSave(AlipayUserInfo alipayUserInfo) {
        return toAjax(merchantInfoEntityService.updateAlipayUserInfoDealUrlByObj(alipayUserInfo));
    }

}
