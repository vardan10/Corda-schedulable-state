package com.heartbeat

import co.paralleluniverse.fibers.Suspendable
import net.corda.core.contracts.Command
import net.corda.core.flows.FinalityFlow
import net.corda.core.flows.FlowLogic
import net.corda.core.flows.InitiatingFlow
import net.corda.core.flows.StartableByRPC
import net.corda.core.transactions.TransactionBuilder
import net.corda.core.utilities.ProgressTracker
import java.time.Instant

/**
 * Creates a Heartbeat state on the ledger.
 *
 * Every Heartbeat state has a scheduled activity to start a flow to consume itself and produce a
 * new Heartbeat state on the ledger after five seconds.
 *
 * By consuming the existing Heartbeat state and creating a new one, a new scheduled activity is
 * created.
 */
@InitiatingFlow
@StartableByRPC
class StartHeartbeatFlow(val nextActivityTime: String) : FlowLogic<Unit>() {
    companion object {
        object GENERATING_TRANSACTION : ProgressTracker.Step("Generating a HeartState transaction.")
        object SIGNING_TRANSACTION : ProgressTracker.Step("Signing transaction with our private key.")
        object FINALISING_TRANSACTION : ProgressTracker.Step("Recording transaction.") {
            override fun childProgressTracker() = FinalityFlow.tracker()
        }

        fun tracker() = ProgressTracker(
                GENERATING_TRANSACTION,
                SIGNING_TRANSACTION,
                FINALISING_TRANSACTION
        )
    }

    override val progressTracker = tracker()

    @Suspendable
    override fun call() {
        progressTracker.currentStep = GENERATING_TRANSACTION


        var time = Instant.now()
        if (nextActivityTime == "null"){
            time = null
        } else {
            try{
                time = Instant.parse(nextActivityTime)
            } catch (e: Exception) {
                time = Instant.now().plusSeconds(nextActivityTime.toLong())
            }
        }
        val output = HeartState(ourIdentity, time)
        val cmd = Command(HeartContract.Commands.Beat(), ourIdentity.owningKey)
        val txBuilder = TransactionBuilder(serviceHub.networkMapCache.notaryIdentities.first())
                .addOutputState(output, HeartContract.contractID)
                .addCommand(cmd)

        progressTracker.currentStep = SIGNING_TRANSACTION
        val signedTx = serviceHub.signInitialTransaction(txBuilder)

        progressTracker.currentStep = FINALISING_TRANSACTION
        subFlow(FinalityFlow(signedTx, FINALISING_TRANSACTION.childProgressTracker()))
    }
}