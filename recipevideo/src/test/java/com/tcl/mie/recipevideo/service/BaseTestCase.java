package com.tcl.mie.recipevideo.service;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:test-servlet.xml", "classpath*:spring-dal.xml" })
public class BaseTestCase {
	{
		System.setProperty("druid.log.stmt.executableSql", "true");
	}
}
