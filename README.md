# Event scheduling

## Two Steps to follow:
  1. State needs to implement ```SchedulableState```<br/>
    Override method ```nextScheduledActivity``` which returns ```ScheduledActivity```<br/>
    
  2. Implement a ```FlowLogic``` to be executed by each node<br/>
    The FlowLogic must be annotated with ```@SchedulableFlow```.

## Note
1. Activities associated with any consumed states are unscheduled.
2. Any newly produced states are then queried via the nextScheduledActivity method and if they do not return null then that activity is scheduled based on the content of the ScheduledActivity object returned.




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

    flow start StartHeartbeatFlow

If you now start monitoring the node's flow activity...

    flow watch

...you will see the `Heartbeat` flow running every second until you close the Flow Watch window using `ctrl/cmd + c`:

    xxxxxxxx-xxxx-xxxx-xx Heartbeat xxxxxxxxxxxxxxxxxxxx Lub-dub

