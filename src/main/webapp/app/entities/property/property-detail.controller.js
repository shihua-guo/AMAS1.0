(function() {
    'use strict';

    angular
        .module('amasApp')
        .controller('PropertyDetailController', PropertyDetailController);

    PropertyDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Property', 'Association'];

    function PropertyDetailController($scope, $rootScope, $stateParams, previousState, entity, Property, Association) {
        var vm = this;

        vm.property = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('amasApp:propertyUpdate', function(event, result) {
            vm.property = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
