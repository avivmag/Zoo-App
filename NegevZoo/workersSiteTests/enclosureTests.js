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
    startNewEnclosure();
    startFailEnclosureName();
    startSuccessEnclosure();
}

function startNewEnclosure() {
    driver.findElement(By.name('enclosures')).sendKeys(webdriver.Key.ENTER);

    driver.sleep(1500).then(function () {
            driver.findElement(By.name('new-enclosure')).click();
    });
}

function startFailEnclosureName() {
    driver.findElement(By.name('save')).click();
    
    var failedEnclosureNameOutput = '';
    
    driver.sleep(500).then(
        function() {
            driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                  function (text) {
                    if (text === 'אנא בחר שם למתחם') {
                        failedEnclosureNameOutput = 'passed';
    
                        testResults.push({testName: 'failed-enclosure-name', testOutput: failedEnclosureNameOutput});
                    }
                    else {
                        failedEnclosureNameOutput = 'failed - alert shown was incorrect: ' + text;
    
                        testResults.push({testName: 'failed-enclosure-name', testOutput: failedEnclosureNameOutput});
                    }
                },
                function () {
                    failedEnclosureNameOutput = 'failed - no alert was shown.';
    
                    testResults.push({testName: 'failed-enclosure-name', testOutput: failedEnclosureNameOutput});
                });
    
            driver.findElement(By.className('md-confirm-button')).click();
        });
}

function startSuccessEnclosure() {
    driver.findElement(By.name('enclosure-name')).sendKeys('new enclosure');
    
    driver.sleep(500).then(function () {
          driver.findElement(By.name('save')).click();
    });
    
    var successEnclosureOutput = '';
    
    driver.sleep(5000).then(
        function() {
            driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                  function (text) {
                    if (text === 'המתחם נוסף בהצלחה!') {
                        successEnclosureOutput = 'passed';
    
                        testResults.push({testName: 'success-enclosure', testOutput: successEnclosureOutput});

                        driver.findElement(By.className('md-confirm-button')).click();

                        startEnclosureTests();
                    }
                    else {
                        successEnclosureOutput = 'failed - alert shown was incorrect: ' + text;
    
                        testResults.push({testName: 'success-enclosure', testOutput: successEnclosureOutput});
                        
                        driver.findElement(By.className('md-confirm-button')).click();

                        finishTests();
                    }
                },
                function () {
                    successEnclosureOutput = 'failed - no alert was shown.';
    
                    testResults.push({testName: 'success-enclosure', testOutput: successEnclosureOutput});

                    driver.findElement(By.className('md-confirm-button')).click();

                    finishTests();
                });
          });
}

function startEnclosureTests() {
    startFailRecurringEventNoTitle();
    startFailRecurringEventNoDescription();
    startFailRecurringEventNoDay();
    startFailRecurringEventNoStartMinute();
    startFailRecurringEventNoStartHour();
    startFailRecurringEventNoEndMinute();
    startFailRecurringEventNoEndHour();
    startSuccessRecurringEvent();
    startDeleteRecurringEvent();
    startFailVideoWrongUrl();
    startSuccessVideo();
    startDeleteVideo();
    startSuccessEnclosureDetail();
    startDeleteEnclosure();
    finishTests();
}

function startFailRecurringEventNoTitle() {
    driver.findElement(By.name('description-save')).sendKeys('new description');
    
    driver.sleep(500).then(function () {
          driver.findElement(By.name('save-recurring')).click();
    });
    
    var output = '';
    
    driver.sleep(500).then(
        function() {
            driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                  function (text) {
                    if (text === 'אנא בחר כותרת לאירוע החוזר.') {
                        output = 'passed';
    
                        testResults.push({testName: 'failed-recurring-event-no-title', testOutput: output});
                    }
                    else {
                        output = 'failed - alert shown was incorrect: ' + text;
    
                        testResults.push({testName: 'failed-recurring-event-no-title', testOutput: output});
                    }
                },
                function () {
                    output = 'failed - no alert was shown.';
    
                    testResults.push({testName: 'failed-recurring-event-no-title', testOutput: output});
                });
    
            driver.findElement(By.className('md-confirm-button')).click();
          });
}

function startFailRecurringEventNoDescription() {
    driver.findElement(By.name('description-save')).clear();
    driver.findElement(By.name('title-save')).sendKeys('new title');
    
    driver.sleep(500).then(function () {
          driver.findElement(By.name('save-recurring')).click();
    });
    
    var output = '';
    
    driver.sleep(500).then(
        function() {
            driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                  function (text) {
                    if (text === 'אנא בחר תיאור לאירוע החוזר.') {
                        output = 'passed';
    
                        testResults.push({testName: 'failed-recurring-event-no-description', testOutput: output});
                    }
                    else {
                        output = 'failed - alert shown was incorrect: ' + text;
    
                        testResults.push({testName: 'failed-recurring-event-no-description', testOutput: output});
                    }
                },
                function () {
                    output = 'failed - no alert was shown.';
    
                    testResults.push({testName: 'failed-recurring-event-no-description', testOutput: output});
                });
    
            driver.findElement(By.className('md-confirm-button')).click();
          });
}

function startFailRecurringEventNoDay() {
    driver.findElement(By.name('description-save')).clear();
    driver.findElement(By.name('title-save')).clear();
    driver.findElement(By.name('title-save')).sendKeys('new title');
    driver.findElement(By.name('description-save')).sendKeys('new description');
    
    driver.sleep(500).then(function () {
          driver.findElement(By.name('save-recurring')).click();
    });
    
    var output = '';
    
    driver.sleep(500).then(
        function() {
            driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                  function (text) {
                    if (text === 'אנא בחר יום תקין לאירוע החוזר.') {
                        output = 'passed';
    
                        testResults.push({testName: 'failed-recurring-event-no-day', testOutput: output});
                    }
                    else {
                        output = 'failed - alert shown was incorrect: ' + text;
    
                        testResults.push({testName: 'failed-recurring-event-no-day', testOutput: output});
                    }
                },
                function () {
                    output = 'failed - no alert was shown.';
    
                    testResults.push({testName: 'failed-recurring-event-no-day', testOutput: output});
                });
    
            driver.findElement(By.className('md-confirm-button')).click();
          });
}

function startFailRecurringEventNoStartMinute() {
    driver.findElement(By.name('description-save')).clear();
    driver.findElement(By.name('title-save')).clear();
    driver.findElement(By.name('title-save')).sendKeys('new title');
    driver.findElement(By.name('description-save')).sendKeys('new description');
    driver.findElement(By.name('day-select')).sendKeys(webdriver.Key.ENTER);
    driver.findElement(By.name('day0')).sendKeys(webdriver.Key.ENTER);
    driver.findElement(By.name('day0')).sendKeys(webdriver.Key.ESCAPE);
    
    driver.sleep(1500).then(function () {
          driver.findElement(By.name('save-recurring')).sendKeys(webdriver.Key.ENTER);
    });
    
    var output = '';
    
    driver.sleep(500).then(
        function() {
            driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                  function (text) {
                    if (text === 'אנא בחר שעת התחלה תקינה.') {
                        output = 'passed';
    
                        testResults.push({testName: 'failed-recurring-event-no-start-minute', testOutput: output});
                    }
                    else {
                        output = 'failed - alert shown was incorrect: ' + text;
    
                        testResults.push({testName: 'failed-recurring-event-no-start-minute', testOutput: output});
                    }
                },
                function () {
                    output = 'failed - no alert was shown.';
    
                    testResults.push({testName: 'failed-recurring-event-no-start-minute', testOutput: output});
                });
    
            driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);
          });
}

function startFailRecurringEventNoStartHour() {
    driver.findElement(By.name('description-save')).clear();
    driver.findElement(By.name('title-save')).clear();
    driver.findElement(By.name('title-save')).sendKeys('new title');
    driver.findElement(By.name('description-save')).sendKeys('new description');

    driver.sleep(1500).then(function () {
        driver.findElement(By.name('day-select')).sendKeys(webdriver.Key.ENTER);
        driver.findElement(By.name('day0')).sendKeys(webdriver.Key.ENTER);
        driver.findElement(By.name('day0')).sendKeys(webdriver.Key.ESCAPE);
    });

    driver.sleep(2000).then(function () {
        driver.findElement(By.name('start-minute-select')).sendKeys(webdriver.Key.ENTER);
        driver.findElement(By.name('start-minute0')).sendKeys(webdriver.Key.ENTER);
        driver.findElement(By.name('start-minute0')).sendKeys(webdriver.Key.ESCAPE);
    });
    
    driver.sleep(1500).then(function () {
          driver.findElement(By.name('save-recurring')).sendKeys(webdriver.Key.ENTER);
    });
    
    var output = '';
    
    driver.sleep(500).then(
        function() {
            driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                  function (text) {
                    if (text === 'אנא בחר שעת התחלה תקינה.') {
                        output = 'passed';
    
                        testResults.push({testName: 'failed-recurring-event-no-start-hour', testOutput: output});
                    }
                    else {
                        output = 'failed - alert shown was incorrect: ' + text;
    
                        testResults.push({testName: 'failed-recurring-event-no-start-hour', testOutput: output});
                    }
                },
                function () {
                    output = 'failed - no alert was shown.';
    
                    testResults.push({testName: 'failed-recurring-event-no-start-hour', testOutput: output});
                });
    
            driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);
          });
}

function startFailRecurringEventNoEndMinute() {
    driver.findElement(By.name('description-save')).clear();
    driver.findElement(By.name('title-save')).clear();
    driver.findElement(By.name('title-save')).sendKeys('new title');
    driver.findElement(By.name('description-save')).sendKeys('new description');

    driver.sleep(1500).then(function () {
        driver.findElement(By.name('day-select')).sendKeys(webdriver.Key.ENTER);
        driver.findElement(By.name('day0')).sendKeys(webdriver.Key.ENTER);
        driver.findElement(By.name('day0')).sendKeys(webdriver.Key.ESCAPE);
    });

    driver.sleep(2000).then(function () {
        driver.findElement(By.name('start-minute-select')).sendKeys(webdriver.Key.ENTER);
        driver.findElement(By.name('start-minute0')).sendKeys(webdriver.Key.ENTER);
        driver.findElement(By.name('start-minute0')).sendKeys(webdriver.Key.ESCAPE);
    });

    driver.sleep(2000).then(function () {
        driver.findElement(By.name('start-hour-select')).sendKeys(webdriver.Key.ENTER);
        driver.findElement(By.name('start-hour0')).sendKeys(webdriver.Key.ENTER);
        driver.findElement(By.name('start-hour0')).sendKeys(webdriver.Key.ESCAPE);
    });
    
    driver.sleep(1500).then(function () {
          driver.findElement(By.name('save-recurring')).sendKeys(webdriver.Key.ENTER);
    });
    
    var output = '';
    
    driver.sleep(500).then(
        function() {
            driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                  function (text) {
                    if (text === 'אנא בחר שעת סוף תקינה.') {
                        output = 'passed';
    
                        testResults.push({testName: 'failed-recurring-event-no-end-minute', testOutput: output});
                    }
                    else {
                        output = 'failed - alert shown was incorrect: ' + text;
    
                        testResults.push({testName: 'failed-recurring-event-no-end-minute', testOutput: output});
                    }
                },
                function () {
                    output = 'failed - no alert was shown.';
    
                    testResults.push({testName: 'failed-recurring-event-no-end-minute', testOutput: output});
                });
    
            driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);
          });
}

function startFailRecurringEventNoEndHour() {
    driver.findElement(By.name('description-save')).clear();
    driver.findElement(By.name('title-save')).clear();
    driver.findElement(By.name('title-save')).sendKeys('new title');
    driver.findElement(By.name('description-save')).sendKeys('new description');

    driver.sleep(1500).then(function () {
        driver.findElement(By.name('day-select')).sendKeys(webdriver.Key.ENTER);
        driver.findElement(By.name('day0')).sendKeys(webdriver.Key.ENTER);
        driver.findElement(By.name('day0')).sendKeys(webdriver.Key.ESCAPE);
    });

    driver.sleep(1500).then(function () {
        driver.findElement(By.name('start-minute-select')).sendKeys(webdriver.Key.ENTER);
        driver.findElement(By.name('start-minute0')).sendKeys(webdriver.Key.ENTER);
        driver.findElement(By.name('start-minute0')).sendKeys(webdriver.Key.ESCAPE);
    });

    driver.sleep(1500).then(function () {
        driver.findElement(By.name('start-hour-select')).sendKeys(webdriver.Key.ENTER);
        driver.findElement(By.name('start-hour0')).sendKeys(webdriver.Key.ENTER);
        driver.findElement(By.name('start-hour0')).sendKeys(webdriver.Key.ESCAPE);
    });
    
    driver.sleep(1500).then(function () {
        driver.findElement(By.name('end-minute-select')).sendKeys(webdriver.Key.ENTER);
        driver.findElement(By.name('end-minute0')).sendKeys(webdriver.Key.ENTER);
        driver.findElement(By.name('end-minute0')).sendKeys(webdriver.Key.ESCAPE);
    });

    driver.sleep(1500).then(function () {
          driver.findElement(By.name('save-recurring')).sendKeys(webdriver.Key.ENTER);
    });
    
    var output = '';
    
    driver.sleep(500).then(
        function() {
            driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                  function (text) {
                    if (text === 'אנא בחר שעת סוף תקינה.') {
                        output = 'passed';
    
                        testResults.push({testName: 'failed-recurring-event-no-end-hour', testOutput: output});
                    }
                    else {
                        output = 'failed - alert shown was incorrect: ' + text;
    
                        testResults.push({testName: 'failed-recurring-event-no-end-hour', testOutput: output});
                    }
                },
                function () {
                    output = 'failed - no alert was shown.';
    
                    testResults.push({testName: 'failed-recurring-event-no-end-hour', testOutput: output});
                });
    
            driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);
          });
}

function startSuccessRecurringEvent() {
    driver.findElement(By.name('description-save')).clear();
    driver.findElement(By.name('title-save')).clear();
    driver.findElement(By.name('title-save')).sendKeys('new title');
    driver.findElement(By.name('description-save')).sendKeys('new description');

    driver.sleep(1500).then(function () {
        driver.findElement(By.name('day-select')).sendKeys(webdriver.Key.ENTER);
        driver.findElement(By.name('day0')).sendKeys(webdriver.Key.ENTER);
        driver.findElement(By.name('day0')).sendKeys(webdriver.Key.ESCAPE);
    });

    driver.sleep(1500).then(function () {
        driver.findElement(By.name('start-minute-select')).sendKeys(webdriver.Key.ENTER);
        driver.findElement(By.name('start-minute0')).sendKeys(webdriver.Key.ENTER);
        driver.findElement(By.name('start-minute0')).sendKeys(webdriver.Key.ESCAPE);
    });

    driver.sleep(1500).then(function () {
        driver.findElement(By.name('start-hour-select')).sendKeys(webdriver.Key.ENTER);
        driver.findElement(By.name('start-hour0')).sendKeys(webdriver.Key.ENTER);
        driver.findElement(By.name('start-hour0')).sendKeys(webdriver.Key.ESCAPE);
    });

    driver.sleep(1500).then(function () {
        driver.findElement(By.name('end-minute-select')).sendKeys(webdriver.Key.ENTER);
        driver.findElement(By.name('end-minute1')).sendKeys(webdriver.Key.ENTER);
        driver.findElement(By.name('end-minute1')).sendKeys(webdriver.Key.ESCAPE);
    });

    driver.sleep(1500).then(function () {
        driver.findElement(By.name('end-hour-select')).sendKeys(webdriver.Key.ENTER);
        driver.findElement(By.name('end-hour1')).sendKeys(webdriver.Key.ENTER);
        driver.findElement(By.name('end-hour1')).sendKeys(webdriver.Key.ESCAPE);
    });
    
    driver.sleep(1500).then(function () {
          driver.findElement(By.name('save-recurring')).sendKeys(webdriver.Key.ENTER);
    });
    
    var output = '';
    
    driver.sleep(5000).then(
        function() {
            driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                  function (text) {
                    if (text === 'האירוע החוזר נוסף בהצלחה!') {
                        output = 'passed';
    
                        testResults.push({testName: 'success-recurring-event', testOutput: output});

                        driver.findElement(By.className('md-confirm-button')).click();
                    }
                    else {
                        output = 'failed - alert shown was incorrect: ' + text;
    
                        testResults.push({testName: 'success-recurring-event', testOutput: output});
                        
                        driver.findElement(By.className('md-confirm-button')).click();
                    }
                },
                function () {
                    output = 'failed - no alert was shown.';
    
                    testResults.push({testName: 'success-recurring-event', testOutput: output});

                    driver.findElement(By.className('md-confirm-button')).click();
                });
          });
}

function startDeleteRecurringEvent() {
    var deleteButton = driver.findElement(By.name('delete-last'))
    
    driver.executeScript("arguments[0].scrollIntoView()", deleteButton);

    deleteButton.sendKeys(webdriver.Key.ENTER);

    let deleteFeedWallOutput = '';
    driver.sleep(500).then(
        function() {
            driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);

            driver.sleep(4500).then(
                function() {
                    driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                        function (text) {
                            if (text === 'האירוע החוזר נמחק בהצלחה.') {
                                deleteFeedWallOutput = 'passed';
            
                                testResults.push({testName: 'delete-recurring-event', testOutput: deleteFeedWallOutput});

                                driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);
                            }
                            else {
                                deleteFeedWallOutput = 'failed - alert shown was incorrect: ' + text;
            
                                testResults.push({testName: 'delete-recurring-event', testOutput: deleteFeedWallOutput});

                                driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);
                            }
                        },
                        function () {
                            deleteFeedWallOutput = 'failed - no alert was shown.';
            
                            testResults.push({testName: 'delete-recurring-event', testOutput: deleteFeedWallOutput});
                            
                            driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);
                        });
    
                })
          });
}

function startFailVideoWrongUrl() {
    var videoUrl = driver.findElement(By.name('videoUrl'));
    
    driver.executeScript("arguments[0].scrollIntoView()", videoUrl);

    videoUrl.sendKeys('www.wrongUrl.com');

    driver.findElement(By.name('addVideo')).sendKeys(webdriver.Key.ENTER);

    var output = '';
    
    driver.sleep(500).then(
        function() {
            driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                  function (text) {
                    if (text === 'אנא הכנס לינק תקין של יוטיוב.') {
                        output = 'passed';
    
                        testResults.push({testName: 'failed-youtube-video-wrong-url', testOutput: output});
                    }
                    else {
                        output = 'failed - alert shown was incorrect: ' + text;
    
                        testResults.push({testName: 'failed-youtube-video-wrong-url', testOutput: output});
                    }
                },
                function () {
                    output = 'failed - no alert was shown.';
    
                    testResults.push({testName: 'failed-youtube-video-wrong-url', testOutput: output});
                });
    
            driver.findElement(By.className('md-confirm-button')).click();
          });
}

function startSuccessVideo() {
    var videoUrl = driver.findElement(By.name('videoUrl'));
    
    driver.executeScript("arguments[0].scrollIntoView()", videoUrl);

    videoUrl.sendKeys('https://www.youtube.com/watch?v=kmQvxNf-9bs');

    driver.findElement(By.name('addVideo')).sendKeys(webdriver.Key.ENTER);

    var output = '';
    
    driver.sleep(5000).then(
        function() {
            driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                  function (text) {
                    if (text === 'הסרטון הועלה בהצלחה!') {
                        output = 'passed';
    
                        testResults.push({testName: 'success-youtube-video', testOutput: output});
                    }
                    else {
                        output = 'failed - alert shown was incorrect: ' + text;
    
                        testResults.push({testName: 'success-youtube-video', testOutput: output});
                    }
                },
                function () {
                    output = 'failed - no alert was shown.';
    
                    testResults.push({testName: 'success-youtube-video', testOutput: output});
                });
    
            driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);
          });
}

function startDeleteVideo() {
    var deleteVideo = driver.findElement(By.name('delete-video0'));
    
    driver.executeScript("arguments[0].scrollIntoView()", deleteVideo);

    deleteVideo.sendKeys(webdriver.Key.ENTER);

    let output = '';
    driver.sleep(1000).then(
        function() {
            driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);

            driver.sleep(4500).then(
                function() {
                    driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                        function (text) {
                            if (text === 'הסרטון נמחק בהצלחה.') {
                                output = 'passed';
            
                                testResults.push({testName: 'delete-youtube-video', testOutput: output});

                                driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);
                            }
                            else {
                                output = 'failed - alert shown was incorrect: ' + text;
            
                                testResults.push({testName: 'delete-youtube-video', testOutput: output});

                                driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);
                            }
                        },
                        function () {
                            output = 'failed - no alert was shown.';
            
                            testResults.push({testName: 'delete-youtube-video', testOutput: output});

                            driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);
                        });
                })
          });
}

function startSuccessEnclosureDetail() {
    var detailName = driver.findElement(By.name('detail-name0'));
    
    driver.executeScript("arguments[0].scrollIntoView()", detailName);

    detailName.sendKeys('מתחם חדש');

    driver.findElement(By.name('detail-story0')).sendKeys('סיפור של מתחם חדש');

    driver.findElement(By.name('add-detail')).sendKeys(webdriver.Key.ENTER);

    var output = '';
    
    driver.sleep(5000).then(
        function() {
            driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                  function (text) {
                    if (text === 'המתחם עודכן בהצלחה!') {
                        output = 'passed';
    
                        testResults.push({testName: 'success-enclosure-detail', testOutput: output});
                    }
                    else {
                        output = 'failed - alert shown was incorrect: ' + text;
    
                        testResults.push({testName: 'success-enclosure-detail', testOutput: output});
                    }
                },
                function () {
                    output = 'failed - no alert was shown.';
    
                    testResults.push({testName: 'success-enclosure-detail', testOutput: output});
                });
    
            driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);
          });
}

function startDeleteEnclosure() {
    var deleteEnclosure = driver.findElement(By.name('delete'));
    
    driver.executeScript("arguments[0].scrollIntoView()", deleteEnclosure);

    deleteEnclosure.sendKeys(webdriver.Key.ENTER);

    let deleteEnclosureOutput = '';
    driver.sleep(500).then(
        function() {
            driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);

            driver.sleep(4500).then(
                function() {
                    driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                        function (text) {
                            if (text === 'המתחם נמחק בהצלחה!') {
                                deleteEnclosureOutput = 'passed';
            
                                testResults.push({testName: 'delete-enclosure', testOutput: deleteEnclosureOutput});

                                driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);
                            }
                            else {
                                deleteEnclosureOutput = 'failed - alert shown was incorrect: ' + text;
            
                                testResults.push({testName: 'delete-enclosure', testOutput: deleteEnclosureOutput});

                                driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);
                            }
                        },
                        function () {
                            deleteEnclosureOutput = 'failed - no alert was shown.';
            
                            testResults.push({testName: 'delete-enclosure', testOutput: deleteEnclosureOutput});

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
