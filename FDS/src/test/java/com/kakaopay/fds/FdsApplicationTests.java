package com.kakaopay.fds;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Connection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.google.gson.Gson;
import com.kakaopay.fds.dao.RuleSetDao;
import com.kakaopay.fds.dao.UserDao;
import com.kakaopay.fds.foundation.DBManager;
import com.kakaopay.fds.foundation.DaoBeanConfig;
import com.kakaopay.fds.foundation.FDSConfig;
import com.kakaopay.fds.response.FraudResponse;
import com.kakaopay.fds.service.FraudDetectService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FdsApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private FraudDetectService fraudDetectService;
	
	private UserDao userDao;
	
	private RuleSetDao ruleSetDao;
	
	private static boolean setUpIsDone = false;
	
	@Before
	public void setup() throws Exception{
		if (setUpIsDone)
			return;
		
		FDSConfig.loadProperties("FDS.properties");
		setUpIsDone = true;
	}
	
	@Test
	public void contextLoads() {
	}

	@Test (timeout=1000)
	public void testFraudDetectController() throws Exception {
		
		//set request
		long userId = 1;
		
		//set response
		FraudResponse fraudResponse = new FraudResponse(userId);
		fraudResponse.setIs_fraud(false);
		fraudResponse.setRule("");
		Gson gson = new Gson();
		String resultString = gson.toJson(fraudResponse);
		
		//test
		this.mockMvc.perform(get("/v1/fraud/" + userId))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(content().string(resultString));
	}
	
	@Test
	public void testFraudDetectService() throws Exception {
		
		//set parameter
		long userId = 2;
		
		//set expectedResult
		String expectedResult = "RuleA";
		
		//test
		assertEquals(expectedResult, fraudDetectService.getViolationRule(userId));
	}
	
	@Test
	public void testUserDao() throws Exception {
		
		//dynamic Dao allocation
		userDao = (UserDao)Class.forName(DaoBeanConfig.UserDao).newInstance();
		
		//set parameter
		Connection conn = DBManager.getInstance().getConnection();
		long userId = 3;
		
		//set expectedResult
		boolean expectedResult = true;
		
		//test
		assertEquals(expectedResult, userDao.isUserAccountExist(conn, userId));
	}
	
	@Test
	public void testRuleSetDao() throws Exception {
		
		//dynamic Dao allocation
		ruleSetDao = (RuleSetDao)Class.forName(DaoBeanConfig.RuleSetDao).newInstance();
		
		//set parameter
		Connection conn = DBManager.getInstance().getConnection();
		long userId = 4;
		
		//set expectedResult
		boolean expectedResultForRuleA = false;
		boolean expectedResultForRuleB = false;
		boolean expectedResultForRuleC = true;
		
		//test
		assertEquals(expectedResultForRuleA, ruleSetDao.isViolatedByRuleA(conn, userId));
		assertEquals(expectedResultForRuleB, ruleSetDao.isViolatedByRuleB(conn, userId));
		assertEquals(expectedResultForRuleC, ruleSetDao.isViolatedByRuleC(conn, userId));
	}
	
}
