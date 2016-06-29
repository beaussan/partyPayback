(function() {
    'use strict';

    angular
        .module('partyPaybackApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('pay-back', {
            parent: 'entity',
            url: '/pay-back?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'partyPaybackApp.payBack.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pay-back/pay-backs.html',
                    controller: 'PayBackController',
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
                    $translatePartialLoader.addPart('payBack');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('pay-back-detail', {
            parent: 'entity',
            url: '/pay-back/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'partyPaybackApp.payBack.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pay-back/pay-back-detail.html',
                    controller: 'PayBackDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('payBack');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'PayBack', function($stateParams, PayBack) {
                    return PayBack.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('pay-back.new', {
            parent: 'pay-back',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pay-back/pay-back-dialog.html',
                    controller: 'PayBackDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                ammount: null,
                                timestamp: null,
                                isPaid: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('pay-back', null, { reload: true });
                }, function() {
                    $state.go('pay-back');
                });
            }]
        })
        .state('pay-back.edit', {
            parent: 'pay-back',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pay-back/pay-back-dialog.html',
                    controller: 'PayBackDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PayBack', function(PayBack) {
                            return PayBack.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('pay-back', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('pay-back.delete', {
            parent: 'pay-back',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pay-back/pay-back-delete-dialog.html',
                    controller: 'PayBackDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PayBack', function(PayBack) {
                            return PayBack.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('pay-back', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
