app.factory('utilitiesService', ['$mdDialog', 
    function ($mdDialog) {
    
        var timeSpan = {
            parseTimeSpan: function(timeSpan) {
                var splitTimeSpan = timeSpan.split(':');
        
                var parsedTimeSpan = {
                    hour:   parseInt(splitTimeSpan[0]),
                    minute: parseInt(splitTimeSpan[1]),
                    second: parseInt(splitTimeSpan[2])
                };
        
                return parsedTimeSpan;
            },

            stringifyTimeSpan: function(timeSpan) {
                return (timeSpan.hour || '00') + ':' + (timeSpan.minute || '00') + ':' + (timeSpan.second || '00');
            },

            getHours: function () {
                var hours = [];

                for (var i = 0; i < 24; i++) {
                    hours.push(i);
                }

                return hours;
            },

            getMinutes: function() {
                var minutes = [];

                for (var i = 0; i < 60; i+=15) {
                    minutes.push(i);
                }

                return minutes;
            }
        };

        function getDays() {
            var days = [
                { id: 1, format: 'ראשון' },
                { id: 2, format: 'שני', },
                { id: 3, format: 'שלישי' },
                { id: 4, format: 'רביעי' },
                { id: 5, format: 'חמישי' },
                { id: 6, format: 'שישי' },
                { id: 7, format: 'שבת' },
            ]

            return days;
        }

        var utilities = {
            alert: function(message) {
                $mdDialog.show(
                    $mdDialog.alert()
                        .clickOutsideToClose(true)
                        .textContent(message)
                        .ok('סגור')
                );
            },

            prompt: function(object) {
                return $mdDialog.show(
                    $mdDialog.prompt()
                        .title(object.title || "")
                        .textContent(object.content || "")
                        .placeholder(object.placeholder || "")
                        .required(object.isRequired || false)
                        .ok(object.okMsg || 'כן')
                        .cancel(object.cancelMsg || 'לא'));
            }
        }

        var utilitiesService = {
            utilities,
            timeSpan,
            getDays
        };

        return utilitiesService;
}]);