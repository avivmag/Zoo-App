app.directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;
          
            element.bind('change', function() {
                scope.$apply(function() {
                    modelSetter(scope, element[0].files);
                });
            });
        }
    };
 }]);

 app.service('fileUpload', ['$http', function ($http) {
    this.uploadFileToUrl = function(files, uploadUrl) {
        uploadUrl   = app.baseURL + uploadUrl;

        var fd      = new FormData();

        for (let i = 0; i < files.length; i++) {
            fd.append('file' + i, files[i]);
        }
    
       return $http.post(uploadUrl, fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined }
        });
    }
 }]);