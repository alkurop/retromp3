package com.omar.retromp3recorder.app.ui.rangebar

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.functions.BiFunction

object RangeBarOutputMapper {
    internal fun mapOutputToState(): ObservableTransformer<RangeBarView.Output, RangeBarView.State> =
        ObservableTransformer { upstream: Observable<RangeBarView.Output> ->
            upstream.scan(RangeBarView.State.Hidden, mapper)
        }

    private val mapper: BiFunction<RangeBarView.State, RangeBarView.Output, RangeBarView.State> =
        BiFunction { _: RangeBarView.State, output: RangeBarView.Output ->
            when (output) {
                is RangeBarView.Output.Visibility -> {
                    if (output.isVisible) {
                        RangeBarView.State.Hidden
                    } else {
                        RangeBarView.State.Hidden
                    }
                }
            }
        }
}