(function() {
    'use strict';

    angular
        .module('amasApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('amember', {
            parent: 'entity',
            url: '/amember?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'amasApp.amember.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/amember/amembers.html',
                    controller: 'AmemberController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('amember');
                    $translatePartialLoader.addPart('global');
                    $translatePartialLoader.addPart('gender');
                    $translatePartialLoader.addPart('politicsstatus');
                    return $translate.refresh();
                }]
            }
        })
        .state('amember-detail', {
            parent: 'amember',
            url: '/amember/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'amasApp.amember.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/amember/amember-detail.html',
                    controller: 'AmemberDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('amember');
                    $translatePartialLoader.addPart('gender');
                    $translatePartialLoader.addPart('politicsstatus');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Amember', function($stateParams, Amember) {
                    return Amember.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'amember',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('amember-detail.edit', {
            parent: 'amember-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/amember/amember-dialog.html',
                    controller: 'AmemberDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Amember', function(Amember) {
                            return Amember.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('amember.new', {
            parent: 'amember',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/amember/amember-dialog.html',
                    controller: 'AmemberDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                membName: null,
                                membNO: null,
                                membClass: null,
                                membPhone: null,
                                membQQ: null,
                                membEmail: null,
                                membJoinDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('amember', null, { reload: 'amember' });
                }, function() {
                    $state.go('amember');
                });
            }]
        })
        .state('amember.edit', {
            parent: 'amember',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/amember/amember-dialog.html',
                    controller: 'AmemberDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Amember', function(Amember) {
                            return Amember.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('amember', null, { reload: 'amember' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('amember.delete', {
            parent: 'amember',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/amember/amember-delete-dialog.html',
                    controller: 'AmemberDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Amember', function(Amember) {
                            return Amember.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('amember', null, { reload: 'amember' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        // 批量导入
        .state('amember.newByExcel', {
            parent: 'amember',
            url: '/newByExcel',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/amember/amember-newByExcel.html',
                    controller: 'fileUploadController'
                }).result.then(function() {
                    $state.go('amember', null, { reload: 'amember' });
                }, function() {
                    $state.go('amember');
                });
            }]
        });
    }

})();
