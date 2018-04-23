package de.uni_hannover.htci.labglasses.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import de.uni_hannover.htci.labglasses.fragments.pager.PageContainerFragment
import de.uni_hannover.htci.labglasses.fragments.pager.RESULT_ITEM
import de.uni_hannover.htci.labglasses.model.Instruction
import org.jetbrains.anko.support.v4.withArguments

/**
 * Created by sl33k on 1/6/18.
 */
class ResultPagerAdapter(fm:FragmentManager?, private val instruction: Instruction) : FragmentStatePagerAdapter(fm) {
    override fun getCount(): Int = instruction.results.size
    override fun getItem(position: Int): Fragment {
        return PageContainerFragment().withArguments(RESULT_ITEM to instruction.results[position])
    }

    override fun getPageTitle(position: Int): CharSequence {
        return "Result #$position"
    }
}
