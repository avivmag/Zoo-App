var webdriver = require('selenium-webdriver'),
    By = webdriver.By,
    until = webdriver.until;

var driver = new webdriver.Builder()
    .forBrowser('firefox')
    .build();

let testResults = [];

driver.get('http://localhost:4646');

startSuccessLogin();

function startSuccessLogin() {
    driver.findElement(By.name('username')).sendKeys('or');

    driver.sleep(500).then(function () {
        driver.findElement(By.name('password')).sendKeys('drdrdr');
    });
    
    driver.sleep(500).then(function () {
          driver.findElement(By.name('login')).click();
    });
    
    var successLoginOutput  = '';
    
    driver.sleep(5000).then(
        function() {
            driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                  function (text) {
                    successLoginOutput = 'failed - alert was shown: ' + text;
    
                    testResults.push({testName: 'success-login', testOutput: successLoginOutput});

                    finishTests();
                },
                function () { });
                
            driver.getTitle().then(function(title) {
                if (title === 'Feed Wall') {
                    successLoginOutput = 'passed';
    
                    testResults.push({testName: 'success-login', testOutput: successLoginOutput});
                    
                    startTests();
                }
                else {
                    testResults.push({testName: 'loginTimeout', testOutput: 'login timeout.'});
    
                    finishTests();
                }
            });
        });
}
    
function startTests() {
    startAboutUs();
    startAddAboutUsNote();
    finishTests();
}

function startAboutUs() {
    driver.findElement(By.name('generalInfo')).sendKeys(webdriver.Key.ENTER);
    
    driver.sleep(1500).then(function () {
        driver.findElement(By.name('about-us')).sendKeys(webdriver.Key.ENTER);

        driver.sleep(2500);
    });
}

function startAddAboutUsNote() {
    driver.sleep(1500).then(function () {
        driver.findElement(By.name('about-us-note')).sendKeys('new about info note');
        driver.findElement(By.name('save-note')).sendKeys(webdriver.Key.ENTER);
    });
    
    var output = '';
    
    driver.sleep(5000).then(
        function() {
            driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                  function (text) {
                    if (text === 'התוכן נשמר בהצלחה') {
                        output = 'passed';
    
                        testResults.push({testName: 'success-about-us-note', testOutput: output});

                        driver.findElement(By.className('md-confirm-button')).click();
                    }
                    else {
                        output = 'failed - alert shown was incorrect: ' + text;
    
                        testResults.push({testName: 'success-about-us-note', testOutput: output});
                        
                        driver.findElement(By.className('md-confirm-button')).click();
                    }
                },
                function () {
                    output = 'failed - no alert was shown.';
    
                    testResults.push({testName: 'success-about-us-note', testOutput: output});

                    driver.findElement(By.className('md-confirm-button')).click();
                });
          });
}

function finishTests() {
    
    driver.sleep(1000).then(function() {
          console.log(testResults);
          // Send log to server.
    });
    
    driver.quit();
}
