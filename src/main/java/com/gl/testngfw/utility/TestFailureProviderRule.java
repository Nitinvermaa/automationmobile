package com.gl.testngfw.utility;

import au.com.dius.pact.consumer.PactVerificationResult;
import au.com.dius.pact.consumer.junit.PactProviderRule;
import au.com.dius.pact.consumer.junit.PactVerification;

import java.util.function.BiConsumer;

public class TestFailureProviderRule extends PactProviderRule {
    private BiConsumer<PactVerificationResult, Throwable> verificationResultConsumer;

    public TestFailureProviderRule(String provider, Object target) {
        super(provider, target);
    }

    @Override
    protected void validateResult(PactVerificationResult result, PactVerification pactVerification) throws Throwable {
        try {
            super.validateResult(result, pactVerification);
            if (verificationResultConsumer != null) {
                verificationResultConsumer.accept(result, null);
            }
        } catch (Exception throwable) {
            if (verificationResultConsumer != null) {
                verificationResultConsumer.accept(result, throwable);
            } else {
                throw throwable;
            }
        }
    }

    public void validateResultWith(BiConsumer<PactVerificationResult, Throwable> function) {
        this.verificationResultConsumer = function;
    }
}
