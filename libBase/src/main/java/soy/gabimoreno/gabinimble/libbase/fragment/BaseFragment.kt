package soy.gabimoreno.gabinimble.libbase.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import soy.gabimoreno.gabinimble.libbase.viewmodel.BaseViewModel

abstract class BaseFragment<
        VIEW_STATE,
        VIEW_EVENT,
        VIEW_MODEL : BaseViewModel<VIEW_STATE, VIEW_EVENT>
        > : StatelessBaseFragment<VIEW_EVENT, VIEW_MODEL>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.viewState.observe(viewLifecycleOwner, Observer(::renderViewState))
    }

    protected abstract fun renderViewState(viewState: VIEW_STATE)
}
