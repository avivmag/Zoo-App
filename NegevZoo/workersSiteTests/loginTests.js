var webdriver 	= require('selenium-webdriver'),
	By 			= webdriver.By,
	until 		= webdriver.until;

var driver 		= new webdriver.Builder()
	.forBrowser('firefox')
	.build();

let testResults = [];

driver.get('http://negevzoo.sytes.net:50555');

startTests();


function startTests() {
	startFailLoginUsername();
	startFailLoginPassword();
	startSuccessLogin();
	
	finishTests();
}

function startFailLoginUsername() {
	driver.findElement(By.name('username')).sendKeys('or123');

	driver.sleep(500).then(function () {
		driver.findElement(By.name('password')).sendKeys('drdrdr');
	});
	
	driver.sleep(500).then(function () {
		  driver.findElement(By.name('login')).click();
	});
	
	var failedLoginUsernameOutput = '';
	
	driver.sleep(500).then(
		function() {
			driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
				function (text) {
					if (text === 'שם משתמש או סיסמא שגויים') {
						failedLoginUsernameOutput = 'passed';
	
						testResults.push({testName: 'failed-login-username', testOutput: failedLoginUsernameOutput});
					}
					else {
						failedLoginUsernameOutput = 'failed - alert shown was incorrect: ' + text;
	
						testResults.push({testName: 'failed-login-username', testOutput: failedLoginUsernameOutput});
					}
				},
				function () {
					failedLoginUsernameOutput = 'failed - no alert was shown.';
	
					testResults.push({testName: 'failed-login-username', testOutput: failedLoginUsernameOutput});
				});
	
			driver.findElement(By.className('md-confirm-button')).click();
		  });
}

function startFailLoginPassword() {
	driver.findElement(By.name('username')).clear();
	driver.findElement(By.name('password')).clear();
	
	driver.findElement(By.name('username')).sendKeys('or');
	
	driver.sleep(500).then(function () {
		driver.findElement(By.name('password')).sendKeys('drdrdrdr');
	});
	
	driver.sleep(500).then(function () {
		  driver.findElement(By.name('login')).click();
	});
	
	var failedLoginPasswordOutput = '';
	
	driver.sleep(500).then(
		function() {
			driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
				  function (text) {
					if (text === 'שם משתמש או סיסמא שגויים') {
						failedLoginPasswordOutput = 'passed';
	
						testResults.push({testName: 'failed-login-password', testOutput: failedLoginPasswordOutput});
					}
					else {
						failedLoginPasswordOutput = 'failed - alert shown was incorrect: ' + text;
	
						testResults.push({testName: 'failed-login-password', testOutput: failedLoginPasswordOutput});
					}
				},
				function () {
					failedLoginPasswordOutput = 'failed - no alert was shown.';
	
					testResults.push({testName: 'failed-login-password', testOutput: failedLoginPasswordOutput});
				});
	
			driver.findElement(By.className('md-confirm-button')).click();
		  });
}

function startSuccessLogin() {
	driver.findElement(By.name('username')).clear();
	driver.findElement(By.name('password')).clear();
	
	driver.findElement(By.name('username')).sendKeys('or');
	
	driver.sleep(500).then(function () {
		driver.findElement(By.name('password')).sendKeys('drdrdr');
	});
	
	driver.sleep(500).then(function () {
		  driver.findElement(By.name('login')).click();
	});
	
	var successLoginOutput = '';
	
	driver.sleep(5000).then(
		function() {
			driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
				  function (text) {
					successLoginOutput = 'failed - alert was shown: ' + text;
	
					testResults.push({testName: 'success-login', testOutput: successLoginOutput});
				},
				function () { });
		  });
		  
	driver.sleep(1500).then(
		function() {
			driver.getTitle().then(function(title) {
				if (title === 'Feed Wall') {
					successLoginOutput = 'passed';
	
					testResults.push({testName: 'success-login', testOutput: successLoginOutput});
				}
			});
		});
}

function finishTests() {
	driver.sleep(1000).then(function() {
		  console.log(testResults);
	});
	
	driver.quit();
}