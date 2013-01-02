controller.registerCommand({

  key:"loadUnit",

  // -----------------------------------------------------------------------
  condition: function( data ){
    var selectedUnitId = data.getSourceUnitId();
    if( selectedUnitId === -1 || data.getSourceUnit().owner !== model.turnOwner ) return false;

    var transporterId = data.getTargetUnitId();
    if( transporterId === -1 || data.getTargetUnit().owner !== model.turnOwner ) return false;

    return (
      model.isTransport( transporterId ) &&
      model.canLoad( selectedUnitId, transporterId )
    );
  },

  // -----------------------------------------------------------------------
  action: function( data ){
    var selectedUnitId = data.getSourceUnitId();
    var transporterId = data.getTargetUnitId();

    model.eraseUnitPosition( selectedUnitId );
    model.loadUnitInto( selectedUnitId, transporterId );
  }
});