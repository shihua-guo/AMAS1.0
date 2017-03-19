(function() {
    'use strict';

    angular
        .module('amasApp')
        .controller('AmembersOfAssoController', AmembersOfAssoController);

    AmembersOfAssoController.$inject = ['$scope','assoAmemNumService','AmembersOfAsso', 'AmembersOfAssoSearch', 'ParseLinks', 'AlertService', 'paginationConstants', '$state', 'pagingParams'];

    function AmembersOfAssoController($scope,assoAmemNumService,AmembersOfAsso, AmembersOfAssoSearch, ParseLinks, AlertService, paginationConstants, $state, pagingParams) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;
        vm.searchQuery = pagingParams.search;
        vm.currentSearch = pagingParams.search;


        loadAll();
        // $rootScope.previousState;
        // $rootScope.currentState;
        /*
        $scope.$on('$stateChangeSuccess', function(ev, to, toParams, from, fromParams) {
            $scope.previousState = from.name;
            $scope.currentState = to.name;
            alert('Previous state:'+$scope.previousState)
            alert('Current state:'+$scope.currentState)
            if ($scope.previousState == 'association-detail') {

            }else if($scope.previousState == 'amember' && $scope.currentState == 'amember'){

            }else{
                assoAmemNumService.setAssoId('');
            }
        });
        */

        function loadAll () {
            if (pagingParams.search) {
                alert("按照关键字查询");
                AmembersOfAssoSearch.query({
                    query: pagingParams.search,
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort(),
                    assoId: assoAmemNumService.getAssoId()
                }, onSuccess, onError);
            }
            
            else if(assoAmemNumService.getAssoId() !=''){
                alert("查询社团对应的成员"+assoAmemNumService.getAssoId());
                AmembersOfAsso.queryAmemNum({
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort(),
                    assoId: assoAmemNumService.getAssoId()
                }, onSuccess, onError);
                alert('查询！！！了')
                //assoAmemNumService.setAssoId('');
                /*$rootScope.$on('$stateChangeStart',
                function(){
                    assoAmemNumService.setAssoId('');
                });*/
            }

            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.amembers = data;
                vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }

        function search(searchQuery) {
            if (!searchQuery){
                return vm.clear();
            }
            vm.links = null;
            vm.page = 1;
            vm.predicate = '_score';
            vm.reverse = false;
            vm.currentSearch = searchQuery;
            vm.transition();
        }

        function clear() {
            vm.links = null;
            vm.page = 1;
            vm.predicate = 'id';
            vm.reverse = true;
            vm.currentSearch = null;
            vm.transition();
        }
    }
})();