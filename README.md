# [Event scheduling](https://docs.corda.net/event-scheduling.html)

## Two Steps to follow:
  1. State needs to implement ```SchedulableState```<br/>
    Override method ```nextScheduledActivity``` which returns ```ScheduledActivity```<br/>
    
    ```
    class HeartState() : SchedulableState {

        override fun nextScheduledActivity(thisStateRef: StateRef, flowLogicRefFactory: FlowLogicRefFactory): ScheduledActivity?      
        {

            return ScheduledActivity(flowLogicRefFactory.create(HeartbeatFlow::class.java, thisStateRef), nextActivityTime)

        }

    }
    ```
    
    
  2. Implement a ```FlowLogic``` to be executed by each node<br/>
    The FlowLogic must be annotated with ```@SchedulableFlow```.
    
    ```
    @InitiatingFlow
    @SchedulableFlow
    class HeartbeatFlow(private val stateRef: StateRef) : FlowLogic<String>() {
        ...
    }
    ```

## Note
1. Activities associated with any **consumed states are unscheduled**.
2. Any newly produced states are then queried via the nextScheduledActivity method and **if they do not return null then that activity is scheduled** based on the content of the ScheduledActivity object returned.


## Example
```
override fun nextScheduledActivity(thisStateRef: StateRef, flowLogicRefFactory: FlowLogicRefFactory): ScheduledActivity? {
    val nextFixingOf = nextFixingOf() ?: return null

    // This is perhaps not how we should determine the time point in the business day, but instead expect the schedule to detail some of these aspects
    val instant = suggestInterestRateAnnouncementTimeWindow(index = nextFixingOf.name, source = floatingLeg.indexSource, date = nextFixingOf.forDay).fromTime!!
    return ScheduledActivity(flowLogicRefFactory.create("net.corda.irs.flows.FixingFlow\$FixingRoleDecider", thisStateRef), instant)
}
```






Original Project [here](https://github.com/corda/samples/tree/release-V3/heartbeat)

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
