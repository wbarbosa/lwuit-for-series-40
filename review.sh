#!/bin/bash

die() {
	echo $1 >&2
	exit $2
}

# check programs and preconditions
[ -n "$(which git)" ] || die "Cannot find git in PATH" 1
[ -n "$(which post-review)" ] || die "Cannot find post-review in PATH" 2
[ -n "$(git config reviewboard.url)" ] || \
	die "reviewboard.url not set in git config" 3
[ -f ".reviewboardrc" ] || die ".reviewboardrc file not found" 4

# check for 'review' remote
git remote|grep -q review || \
	die "Please add git remote for reviewboard as 'review'" 5

# get changesets
read lastrev firstrev \
	<<< $(git log --format=format:%H review/master..master|tr "\\n" " ")

if [ -z "$firstrev" ]; then
	firstrev=$lastrev
	revarg="$lastrev"
else
	revarg="--revision-range=$firstrev:$lastrev"
fi

# prompt for okay
	git --no-pager log --oneline $firstrev^..$lastrev
echo "Push these revisions for review?"
select yesno in "Yes" "No"; do
	case $yesno in
		Yes)
			break
			;;
		No)
			exit
			;;
	esac
done

# push to review
git push review master || die "Git push failed" 6

# post review
post-review --guess-summary --guess-description $@ $revarg || \
	die "post-review failed" 7

echo "Done! Don't forget to publish the review request (unless you specified"
echo "--publish)."
