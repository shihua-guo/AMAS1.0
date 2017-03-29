(function() {
    'use strict';

    angular
        .module('amasApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('association', {
            parent: 'entity',
            url: '/association?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'amasApp.association.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/association/associations.html',
                    controller: 'AssociationController',
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
                    $translatePartialLoader.addPart('association');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('association-detail', {
            parent: 'association',
            url: '/association/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'amasApp.association.detail.title'
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/association/association-detail.html',
                    controller: 'AssociationDetailController',
                    controllerAs: 'vm',
                    resolve: {
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('association');
                            return $translate.refresh();
                        }],
                        entity: [ 'Association', function( Association) {
                            return Association.get({id : $stateParams.id}).$promise;
                        }],
                        previousState: ["$state", function ($state) {
                            var currentStateData = {
                                name: $state.current.name || 'association',
                                params: $state.params,
                                url: $state.href($state.current.name, $state.params)
                            };
                            return currentStateData;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
            /*views: {
                'content@': {
                    templateUrl: 'app/entities/association/association-detail.html',
                    controller: 'AssociationDetailController',
                    controllerAs: 'vm'
                }
            },*/
            /*resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('association');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Association', function($stateParams, Association) {
                    return Association.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'association',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }*/
        })
        .state('association-detail.edit', {
            parent: 'association-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/association/association-dialog.html',
                    controller: 'AssociationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Association', function(Association) {
                            return Association.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('association.new', {
            parent: 'association',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/association/association-dialog.html',
                    controller: 'AssociationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                assoId: null,
                                assoName: null,
                                assoFoundDate: null,
                                assoIntrodution: null,
                                assoImage: null,
                                assoImageContentType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('association', null, { reload: 'association' });
                }, function() {
                    $state.go('association');
                });
            }]
        })
        .state('association.edit', {
            parent: 'association',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/association/association-dialog.html',
                    controller: 'AssociationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Association', function(Association) {
                            return Association.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('association', null, { reload: 'association' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('association.delete', {
            parent: 'association',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/association/association-delete-dialog.html',
                    controller: 'AssociationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Association', function(Association) {
                            return Association.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('association', null, { reload: 'association' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
