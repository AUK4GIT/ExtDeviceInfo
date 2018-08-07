var exec = require('cordova/exec');
var argscheck = require('cordova/argscheck');
var channel = require('cordova/channel');
var utils = require('cordova/utils');
var cordova = require('cordova');

channel.createSticky('onCordovaInfoReady');
// Tell cordova channel to wait on the CordovaInfoReady event
channel.waitForInitialization('onCordovaInfoReady');

function ExtDeviceInfo(){
    this.memory = null;
    this.cpumhz = null;
    this.totalstorage = null;

    var self = this;
    channel.onCordovaReady.subscribe(function () {
        if(cordova.platformId === 'android') {
            self.getRAMSize(function(info){
                self.memory = info.memory || 'unknown';
                self.cpumhz = info.cpumhz || 'unknown';
                self.totalstorage = info.totalstorage || 'unknown';
                channel.onCordovaReady.fire();//Cause for issue
            }, function(e){
                utils.alert('[ERROR] Error initializing Cordova: ' + e);
            });
        }
    });
}

/*
exports.getRAMSize = function (arg0, success, error) {
    exec(success, error, 'ExtDeviceInfo', 'getRAMSize', [arg0]);
};
*/

ExtDeviceInfo.prototype.getRAMSize = function (successCallback, errorCallback) {
    argscheck.checkArgs('fF', 'ExtDeviceInfo.getRAMSize', arguments);
    exec(successCallback, errorCallback, 'ExtDeviceInfo', 'getRAMSize', []);
};

module.exports = new ExtDeviceInfo();
