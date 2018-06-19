var webdriver = require('selenium-webdriver'),
    By = webdriver.By,
    until = webdriver.until;

var driver = new webdriver.Builder()
    .forBrowser('firefox')
    .build();

let testResults = [];

driver.get('http://negevzoo.sytes.net:8080');

driver.sleep(1000).then(function () {
    startSuccessLogin();
});

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
    startPrices();
    startFailPriceNoPrice();
    startFailPriceNoPopulation();
    startFailPriceNegativePrice();
    startSuccessPrice();
    startDeletePrice();
    finishTests();
}

function startPrices() {
    driver.findElement(By.name('generalInfo')).sendKeys(webdriver.Key.ENTER);
    
    driver.sleep(1500).then(function () {
        driver.findElement(By.name('prices')).sendKeys(webdriver.Key.ENTER);

        driver.sleep(2500);
    });
}

function startFailPriceNoPrice() {
    driver.sleep(1500).then(function () {
        driver.findElement(By.name('population')).sendKeys('new population');
        driver.findElement(By.name('save')).sendKeys(webdriver.Key.ENTER);
    });
    
    var output = '';
    
    driver.sleep(500).then(
        function() {
            driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                  function (text) {
                    if (text === 'אנא בחר מחיר חוקי(גדול או שווה ל 0)') {
                        output = 'passed';
    
                        testResults.push({testName: 'failed-price-no-price', testOutput: output});
                    }
                    else {
                        output = 'failed - alert shown was incorrect: ' + text;
    
                        testResults.push({testName: 'failed-price-no-price', testOutput: output});
                    }
                },
                function () {
                    output = 'failed - no alert was shown.';
    
                    testResults.push({testName: 'failed-price-no-price', testOutput: output});
                });
    
            driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);
        });
}

function startFailPriceNoPopulation() {
    driver.sleep(1500).then(function () {
        driver.findElement(By.name('population')).clear();
        driver.findElement(By.name('pricePop')).sendKeys('5');
        driver.findElement(By.name('save')).sendKeys(webdriver.Key.ENTER);
    });
    
    var output = '';
    
    driver.sleep(500).then(
        function() {
            driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                  function (text) {
                    if (text === 'אנא בחר אוכלוסייה') {
                        output = 'passed';
    
                        testResults.push({testName: 'failed-price-no-population', testOutput: output});
                    }
                    else {
                        output = 'failed - alert shown was incorrect: ' + text;
    
                        testResults.push({testName: 'failed-price-no-population', testOutput: output});
                    }
                },
                function () {
                    output = 'failed - no alert was shown.';
    
                    testResults.push({testName: 'failed-price-no-population', testOutput: output});
                });
    
            driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);
        });
}

function startFailPriceNegativePrice() {
    driver.sleep(1500).then(function () {
        driver.findElement(By.name('population')).clear();
        driver.findElement(By.name('pricePop')).clear();
        driver.findElement(By.name('population')).sendKeys('new population');
        driver.findElement(By.name('pricePop')).sendKeys('-5');
        driver.findElement(By.name('save')).sendKeys(webdriver.Key.ENTER);
    });
    
    var output = '';
    
    driver.sleep(500).then(
        function() {
            driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                  function (text) {
                    if (text === 'אנא בחר מחיר חוקי(גדול או שווה ל 0)') {
                        output = 'passed';
    
                        testResults.push({testName: 'failed-price-negative-price', testOutput: output});
                    }
                    else {
                        output = 'failed - alert shown was incorrect: ' + text;
    
                        testResults.push({testName: 'failed-price-negative-price', testOutput: output});
                    }
                },
                function () {
                    output = 'failed - no alert was shown.';
    
                    testResults.push({testName: 'failed-price-negative-price', testOutput: output});
                });
    
            driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);
        });
}

function startSuccessPrice() {
    driver.sleep(1500).then(function () {
        driver.findElement(By.name('population')).clear();
        driver.findElement(By.name('pricePop')).clear();
        driver.findElement(By.name('population')).sendKeys('new population');
        driver.findElement(By.name('pricePop')).sendKeys('10');
        driver.findElement(By.name('save')).sendKeys(webdriver.Key.ENTER);
    });
    
    var output = '';
    
    driver.sleep(5000).then(
        function() {
            driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                  function (text) {
                    if (text === 'המחיר נוסף בהצלחה!') {
                        output = 'passed';
    
                        testResults.push({testName: 'success-price', testOutput: output});

                        driver.findElement(By.className('md-confirm-button')).click();
                    }
                    else {
                        output = 'failed - alert shown was incorrect: ' + text;
    
                        testResults.push({testName: 'success-price', testOutput: output});
                        
                        driver.findElement(By.className('md-confirm-button')).click();
                    }
                },
                function () {
                    output = 'failed - no alert was shown.';
    
                    testResults.push({testName: 'success-price', testOutput: output});

                    driver.findElement(By.className('md-confirm-button')).click();
                });
          });
}

function startDeletePrice() {
    var deleteButton = driver.findElement(By.name('delete-last'))
    
    driver.executeScript("arguments[0].scrollIntoView()", deleteButton);

    deleteButton.sendKeys(webdriver.Key.ENTER);

    let output = '';
    driver.sleep(500).then(
        function() {
            driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);

            driver.sleep(4500).then(
                function() {
                    driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                        function (text) {
                            if (text === 'המחירון נמחק בהצלחה') {
                                output = 'passed';
            
                                testResults.push({testName: 'delete-price', testOutput: output});

                                driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);
                            }
                            else {
                                output = 'failed - alert shown was incorrect: ' + text;
            
                                testResults.push({testName: 'delete-price', testOutput: output});

                                driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);
                            }
                        },
                        function () {
                            output = 'failed - no alert was shown.';
            
                            testResults.push({testName: 'delete-price', testOutput: output});
                            
                            driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);
                        });
    
                })
          });
}

function finishTests() {
    
    driver.sleep(1000).then(function() {
          console.log(testResults);
          // Send log to server.
    });
    
    driver.quit();
}
