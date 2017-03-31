(function () {
    // body...
'use strict';

angular
        .module('amasApp')
        .controller('fileUploadController', fileUploadController);

    fileUploadController.$inject = ['Amember','$scope', 'FileUploader','$cookies','$uibModalInstance'];

    function fileUploadController(Amember,$scope, FileUploader,$cookies,$uibModalInstance) {
        var vm = this;
        vm.clear = clear;
        //和前台页面绑定的ngmodel！！
        $scope.selectAsso = '';
        var url = '';
        //获取社团的名称
        $scope.getAssoIdAndName = Amember.getAssoIdAndName();
        var uploader = $scope.uploader = new FileUploader({
            url: url,
            headers: {
               'X-XSRF-TOKEN': $cookies.get('XSRF-TOKEN')
             }
        });
        // uploader.onBeforeUploadItem = function (item) {
        // item.headers = {'X-XSRF-TOKEN': $cookies['XSRF-TOKEN']};
        // };

        // FILTERS
        function clear () {
              $uibModalInstance.dismiss('cancel');
          };

    // a sync filter
    uploader.filters.push({
        name: 'syncFilter',
        fn: function(item /*{File|FileLikeObject}*/, options) {
            console.log('syncFilter');
            return this.queue.length < 10;
        }
    });
  
    // an async filter
    uploader.filters.push({
        name: 'asyncFilter',
        fn: function(item /*{File|FileLikeObject}*/, options, deferred) {
            console.log('asyncFilter');
            setTimeout(deferred.resolve, 1e3);
        }
    });
    // 文件类型过滤器
    uploader.filters.push({
        name: 'xlsxFilter',
        fn: function(item /*{File|FileLikeObject}*/, options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|vnd.openxmlformats-officedocument.spreadsheetml.sheet|vnd.ms-excel|'.indexOf(type) !== -1;
        }
    });
    // CALLBACKS
    uploader.onWhenAddingFileFailed = function(item /*{File|FileLikeObject}*/, filter, options) {
        console.info('onWhenAddingFileFailed', item, filter, options);
    };
    uploader.onAfterAddingFile = function(fileItem) {
        console.info('onAfterAddingFile', fileItem);
        // fileItem.url  = '/api/fileUpload/'+$scope.selectAsso.id;
    };
    uploader.onAfterAddingAll = function(addedFileItems) {
        console.info('onAfterAddingAll', addedFileItems);
    };
    uploader.onBeforeUploadItem = function(item) {
        item.url  = '/api/fileUpload/'+$scope.selectAsso.id;
    };
    uploader.onProgressItem = function(fileItem, progress) {
        console.info('onProgressItem', fileItem, progress);
        // fileItem.url  = '/api/fileUpload/'+$scope.selectAsso.id;
    };
    uploader.onProgressAll = function(progress) {
        console.info('onProgressAll', progress);
    };
    uploader.onSuccessItem = function(fileItem, response, status, headers) {
        console.info('onSuccessItem', fileItem, response, status, headers);
        alert($scope.selectAsso.assoName+"社团批量添加成功");
    };
    uploader.onErrorItem = function(fileItem, response, status, headers) {
        console.info('onErrorItem', fileItem, response, status, headers);
    };
    uploader.onCancelItem = function(fileItem, response, status, headers) {
        console.info('onCancelItem', fileItem, response, status, headers);
    };
    uploader.onCompleteItem = function(fileItem, response, status, headers) {
        console.info('onCompleteItem', fileItem, response, status, headers);
    };
    uploader.onCompleteAll = function() {
        console.info('onCompleteAll');
        
    };

    console.info('uploader', uploader);
};

})();
