package com.airing.im.wrapper;

import com.airing.im.dao.auth.AuthTestDao;
import com.airing.im.dao.game.GameTestDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GameTestWrapper {

    @Autowired
    private AuthTestDao authTestDao;
    @Autowired
    private GameTestDao gameTestDao;

    @Transactional(transactionManager = "authTransactionManager")
    public void authTest() {
        authTestDao.insert();
    }

    @Transactional(transactionManager = "gameTransactionManager")
    public void gameTest() {
        gameTestDao.insert();
    }

}
