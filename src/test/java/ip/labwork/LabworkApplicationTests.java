package ip.labwork;

import ip.labwork.method.service.MethodService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LabworkApplicationTests {

	@Autowired
	MethodService speakerService;
	@Test
	void testIntSum() {
		final String res = speakerService.Sum(20,10,"int");
		Assertions.assertEquals(30, Integer.parseInt(res));
	}

	@Test
	void testIntMinus() {
		final String res = speakerService.Ras(20,10,"int");
		Assertions.assertEquals(10, Integer.parseInt(res));
	}
	@Test
	void testIntMulti() {
		final String res = speakerService.Pros(20,10,"int");
		Assertions.assertEquals(200, Integer.parseInt(res));
	}
	@Test
	void testIntDiv() {
		final String res = speakerService.Del(20,10,"int");
		Assertions.assertEquals(2, Integer.parseInt(res));
	}
	@Test
	void testStringSum() {
		final String res = speakerService.Sum("20","10","string");
		Assertions.assertEquals("2010", res);
	}

	@Test
	void testStringMinus() {
		final String res = speakerService.Ras("300",1,"string");
		Assertions.assertEquals("30", res);
	}
	@Test
	void testStringMulti() {
		final String res = speakerService.Pros("20",2,"string");
		Assertions.assertEquals("2020", res);
	}
	@Test
	void testStringDiv() {
		final String res = speakerService.Del("20","2","string");
		Assertions.assertEquals("true", res);
	}
	@Test
	void testSpeakerErrorWired() {
		Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> speakerService.Sum("10", "20", "double"));
	}

}
