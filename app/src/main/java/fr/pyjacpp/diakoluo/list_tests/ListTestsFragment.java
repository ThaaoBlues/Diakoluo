package fr.pyjacpp.diakoluo.list_tests;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.RecyclerViewChange;
import fr.pyjacpp.diakoluo.edit_test.EditTestActivity;
import fr.pyjacpp.diakoluo.test_tests.TestSettingsActivity;
import fr.pyjacpp.diakoluo.tests.Test;
import fr.pyjacpp.diakoluo.view_test.ViewTestActivity;


public class ListTestsFragment extends Fragment implements TestAdapter.TestViewListener {
    private OnFragmentInteractionListener mListener;
    private RecyclerView testRecyclerView;
    private RecyclerView.Adapter testRecyclerViewAdapter;

    public ListTestsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedLayout = inflater.inflate(R.layout.fragment_recycler_list, container, false);

        testRecyclerView = inflatedLayout.findViewById(R.id.recyclerView);
        LinearLayoutManager testRecyclerViewLayoutManager = new LinearLayoutManager(getContext());
        testRecyclerViewAdapter = new TestAdapter(getContext(), this);



        testRecyclerView.setHasFixedSize(true);
        testRecyclerView.setLayoutManager(testRecyclerViewLayoutManager);
        testRecyclerView.setAdapter(testRecyclerViewAdapter);

        testRecyclerView.addItemDecoration(new DividerItemDecoration(testRecyclerView.getContext(),
                testRecyclerViewLayoutManager.getOrientation()));

        return inflatedLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
        Context context = getContext();
        if (context != null) {
            RecyclerViewChange testListChanged = DiakoluoApplication.getTestListChanged(context);
            if (testListChanged != null) {
                testListChanged.apply(testRecyclerViewAdapter);
                DiakoluoApplication.setTestListChanged(context, null);
            }
        }
    }

    public interface OnFragmentInteractionListener {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        DiakoluoApplication.setCurrentTest(view.getContext(),
                DiakoluoApplication.getListTest(view.getContext()).get(position));

        startActivity(new Intent(view.getContext(), ViewTestActivity.class));
    }

    @Override
    public void onPlayButtonClick(View view, int position) {
        Test currentTest = DiakoluoApplication.getListTest(view.getContext()).get(position);
        if (currentTest.canBePlay()) {
            DiakoluoApplication.setCurrentTest(view.getContext(),
                    currentTest);

            startActivity(new Intent(view.getContext(), TestSettingsActivity.class));
        } // TODO warning
    }

    @Override
    public void onSeeButtonClick(View view, int position) {
        onItemClick(view, position);
    }

    @Override
    public void onDeleteMenuItemClick(final View view, final int position) {
        final ArrayList<Test> listTest = DiakoluoApplication.getListTest(view.getContext());

        new AlertDialog.Builder(view.getContext())
                .setTitle(R.string.dialog_delete_test_title)
                .setMessage(getString(R.string.dialog_delete_test_message, listTest.get(position).getName()))
                .setCancelable(true)
                .setIcon(R.drawable.ic_delete_forever_accent_color_24dp)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        testRecyclerView.removeViewAt(position);
                        testRecyclerViewAdapter.notifyItemRemoved(position);
                        testRecyclerViewAdapter.notifyItemRangeChanged(position, listTest.size());

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                DiakoluoApplication.removeTest(view.getContext(), position);
                            }
                        }).start();

                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void onEditMenuItemClick(View view, int position) {
        DiakoluoApplication.setCurrentIndexEditTest(view.getContext(), position);
        startActivity(new Intent(view.getContext(), EditTestActivity.class));
    }
}
