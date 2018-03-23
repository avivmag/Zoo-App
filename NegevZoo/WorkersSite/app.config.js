var app = angular.module('visitors_app', ['ngMaterial', 'ui.router']);

app.config(function ($mdDateLocaleProvider) {
    $mdDateLocaleProvider.formatDate = function (date) {
        return moment(date).format('DD-MM-YYYY');
    };
});

app.directive('fileModel', ['$parse', function ($parse) {
    return {
       restrict: 'A',
       link: function(scope, element, attrs) {
          var model = $parse(attrs.fileModel);
          var modelSetter = model.assign;
          
          element.bind('change', function(){
             scope.$apply(function(){
                modelSetter(scope, element[0].files[0]);
             });
          });
       }
    };
 }]);

 app.service('fileUpload', ['$http', function ($http) {
    this.uploadFileToUrl = function(file, uploadUrl) {
        uploadUrl = app.baseURL + uploadUrl;
       var fd = new FormData();
       fd.append('file', file);
    
       return $http.post(uploadUrl, fd, {
        transformRequest: angular.identity,
        headers: {'Content-Type': undefined}
     })
    }
 }]);

app.baseURL = 'http://negevzoo.sytes.net:50000/';

app.baseURL = 'http://localhost:5987/';