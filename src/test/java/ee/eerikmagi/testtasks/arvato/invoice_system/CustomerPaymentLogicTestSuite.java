package ee.eerikmagi.testtasks.arvato.invoice_system;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import ee.eerikmagi.testtasks.arvato.invoice_system.logic.ExamplePaymentLogicTests;
import ee.eerikmagi.testtasks.arvato.invoice_system.logic.PremiumCustomerPaymentLogicTests;
import ee.eerikmagi.testtasks.arvato.invoice_system.logic.RegularCustomerPaymentLogicTests;

@RunWith(Suite.class)
@SuiteClasses({
	RegularCustomerPaymentLogicTests.class,
	PremiumCustomerPaymentLogicTests.class,
	ExamplePaymentLogicTests.class
})
public class CustomerPaymentLogicTestSuite {

}
