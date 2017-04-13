(function() {
    
    angular
        .module('joinApp')
        .factory('Amember', Amember)
        .factory('Association', Association);
    Amember.$inject = ['$resource'];
    Association.$inject = ['$resource'];
    function Amember ($resource) {
        var resourceUrl =  'api/joinAmembers';

        return $resource(resourceUrl, {}, {
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    return angular.toJson(copy);
                }
            }
        });
    }
    function Association ($resource) {
        var resourceUrl =  'api/getAssoIdAndName';
        return $resource(resourceUrl, {}, {
            'query': {
                 method: 'GET', isArray: true
            }
        });
    }

    angular.module('joinApp')
    .controller('AmemberDialogController', AmemberDialogController);
    AmemberDialogController.$inject = ['Amember','Association','$scope','CityData'];
    function AmemberDialogController (Amember,Association,$scope,CityData) {
        $scope.amember = {
            "membName":null,
            "membNO":null,
            "membClass":null,
            "membPhone":null,
            "membQQ":null,
            "membEmail":null,
            "membJoinDate":null,
            "id":null,
            "gender":null,
            "dormNum":null,
            "politicsStatus":null,
            "college":null,
            "major":null,
            "associations":null
        };
        $scope.save = save;
        $scope.associations = Association.query();

        console.log(CityData);
        function save () {
            $scope.isSaving = true;
            if ($scope.amember.college) {
                $scope.amember.college = $scope.amember.college.value;
            }
            if ($scope.amember.major) {
                $scope.amember.major = $scope.amember.major.value;
            }
            if ($scope.amember.associations) {
                var tmpAsso = $scope.amember.associations;
                $scope.amember.associations = [];
                $scope.amember.associations[0] = tmpAsso;
            }
            Amember.save($scope.amember, onSaveSuccess, onSaveError);
        }

        function onSaveSuccess (result) {
            $scope.$emit('amasApp:amemberUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        }

        function onSaveError () {
            $scope.isSaving = false;
        }

        $scope.colleges = CityData;  
          // 更换国家的时候清空省  
          $scope.$watch('$scope.colleges', function(country) {  
            $scope.major = null;  
          });  
          // 更换省的时候清空城市  
          $scope.$watch('vm.province', function(province) {  
            $scope.city = null;  
          });  


    }

    
})();
