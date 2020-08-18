package com.hurl.fundationusealipayopenchain.controller;

import com.hurl.fundationusealipayopenchain.service.BlockchainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 区块链服务
 */
@RestController
@RequestMapping("/blockchain")
public class BlockchainController {
    @Autowired
    BlockchainService blockchainService;

    @PostMapping("/deposit")
    @ResponseBody
    public String deposit(String content) throws Exception {
        return blockchainService.deposit(content);
    }

    @GetMapping("/query-transaction")
    @ResponseBody
    public String queryTransaction(String hash) {
        return blockchainService.queryTransaction(hash);
    }
}
