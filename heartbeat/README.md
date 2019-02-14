<p align="center">
  <img src="https://www.corda.net/wp-content/uploads/2016/11/fg005_corda_b.png" alt="Corda" width="500">
</p>

Original PRoject [here](https://github.com/corda/samples/tree/release-V3/heartbeat)

# Heartbeat CorDapp

This CorDapp is a simple showcase of scheduled activities (i.e. activities started by a node at a specific time without 
direct input from the node owner).

A node starts its heartbeat by calling the `StartHeartbeatFlow`. This creates a `HeartState` on the ledger. This 
`HeartState` has a scheduled activity to start the `HeatbeatFlow` one second later.

When the `HeartbeatFlow` runs one second later, it consumes the existing `HeartState` and creates a new `HeartState`. 
The new `HeartState` also has a scheduled activity to start the `HeatbeatFlow` in one second.

In this way, calling the `StartHeartbeatFlow` creates an endless chain of `HeartbeatFlow`s one second apart.

# Pre-requisites:
  
See https://docs.corda.net/getting-set-up.html.

# Usage

## Running the nodes:

The nodes do not expose a front-end or API, so you need to deploy from from the command line and interact with them via 
the CRaSH shell. See https://docs.corda.net/tutorial-cordapp.html#running-the-example-cordapp.

## Interacting with the nodes:

Go to the CRaSH shell for PartyA, and run the `StartHeatbeatFlow`:

    flow start StartHeartbeatFlow <PARAMETER>
    
    <PARAMETER>: Can pass any one of below three options
        1. Provide Zulu time - Flow will be Scheduled only once @Zulu time
          start StartHeartbeatFlow nextActivityTime: "2019-02-14T04:50:25.510045Z"
        
        2. Provide x Seconds - Flow will run contineously for every x seconds
          start StartHeartbeatFlow nextActivityTime: "5"
        
        3. Provide "null" - Flow will not be scheduled
          start StartHeartbeatFlow nextActivityTime: "null"

If you now start monitoring the node's flow activity...

    flow watch

...you will see the `Heartbeat` flow running every second until you close the Flow Watch window using `ctrl/cmd + c`:

    xxxxxxxx-xxxx-xxxx-xx Heartbeat xxxxxxxxxxxxxxxxxxxx Lub-dub
