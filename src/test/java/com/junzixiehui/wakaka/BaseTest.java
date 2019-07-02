package com.junzixiehui.wakaka;

import org.junit.Test;

import java.util.Random;

/**
 * <p>Description: </p>
 * @author: by zt
 * @date: 2019/6/25  12:07
 * @version: 1.0
 */
public class BaseTest {

	@Test
	public void test() {

		Random random = new Random();
		System.out.println(random.nextInt(2));
	}
}
