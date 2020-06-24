package org.mifos.mobilewallet.core.domain.usecase.journal

import org.mifos.mobilewallet.core.base.UseCase
import org.mifos.mobilewallet.core.data.common.FineractRepository
import org.mifos.mobilewallet.core.data.fineractcn.entity.journal.JournalEntry
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Devansh on 19/06/2020
 */
class FetchJournalEntry @Inject constructor(private val apiRepository: FineractRepository) :
        UseCase<FetchJournalEntry.RequestValues, FetchJournalEntry.ResponseValue>() {

    override fun executeUseCase(requestValues: RequestValues) {
        apiRepository.fetchJournalEntries(requestValues.accountIdentifier, requestValues.dateRange)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<List<JournalEntry>>() {

                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable)
                            = useCaseCallback.onError(e.message)

                    override fun onNext(journalEntryList: List<JournalEntry>) =
                            useCaseCallback.onSuccess(ResponseValue(journalEntryList))
                })
    }

    class RequestValues(val accountIdentifier: String, val dateRange: String) :
            UseCase.RequestValues

    class ResponseValue(val journalEntryList: List<JournalEntry>) : UseCase.ResponseValue

}