package com.hurl.fundationusealipayopenchain.business;

import com.alibaba.fastjson.JSON;
import com.antfinancial.mychain.baas.tool.restclient.RestClient;
import com.antfinancial.mychain.baas.tool.restclient.RestClientProperties;
import com.antfinancial.mychain.baas.tool.restclient.model.ClientParam;
import com.antfinancial.mychain.baas.tool.restclient.model.Method;
import com.antfinancial.mychain.baas.tool.restclient.response.BaseResp;
import com.hurl.fundationusealipayopenchain.dto.BTQueryTransactionResponseData;
import com.hurl.fundationusealipayopenchain.exception.RetryException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BlockchainClient {

    @Autowired
    private RestClient restClient;

    @Autowired
    private RestClientProperties restClientProperties;

    /**
     * 存证上链
     *
     * @param data 存证内容
     * @return
     * @throws Exception
     */
    public String deposit(String data) throws Exception {
        ClientParam clientParam = restClient.createDepositTransaction(restClientProperties.getDefaultAccount(), data, 50000L);
        BaseResp resp = restClient.chainCall(clientParam.getHash(), clientParam.getSignData(), Method.DEPOSIT);
        checkResp(resp);
        log.info("存证上链成功，内容 = {}, hash = {}", data, clientParam.getHash());
        return clientParam.getHash();
    }

    /**
     * 查询存证结果
     *
     * @param hash
     * @return
     */
    public String queryTransaction(String hash) {
        BTQueryTransactionResponseData data = queryTransactionOrigin(hash);
        String transactionData = new String(Base64.decode(data.getTransactionDO().getData()));
        log.info("存证内容 {}", transactionData);
        return transactionData;
    }

    /**
     * 查询存证原始结果
     *
     * @param hash
     * @return
     */
    @Retryable(value = RetryException.class, maxAttempts = 2, backoff = @Backoff(delay = 30000, multiplier = 1))
    public BTQueryTransactionResponseData queryTransactionOrigin(String hash) {
        BaseResp resp;
        try {
            resp = restClient.chainCall(hash, null, Method.QUERYTRANSACTION);
        } catch (Exception e) {
            throw RetryException.create(e.getMessage(), e);
        }
        if (!resp.isSuccess() || !StringUtils.equals(resp.getCode(), "200")) {
            throw new RetryException(JSON.toJSONString(resp));
        }

        log.info("查询到存证结果, resp = {}, hash = {}", JSON.toJSONString(resp), hash);
        return JSON.parseObject(resp.getData(), BTQueryTransactionResponseData.class);
    }


    private void checkResp(BaseResp resp) throws Exception {
        if (!resp.isSuccess() || !StringUtils.equals(resp.getCode(), "200")) {
            throw new Exception(JSON.toJSONString(resp));
        }
    }
}
